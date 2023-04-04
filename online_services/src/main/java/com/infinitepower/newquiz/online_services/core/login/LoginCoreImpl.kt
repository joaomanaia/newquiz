package com.infinitepower.newquiz.online_services.core.login

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.online_services.core.worker.CheckUserDBWorker
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginCoreImpl @Inject constructor(
    private val workManager: WorkManager
) : LoginCore {
    private val auth = Firebase.auth

    override fun loginWithEmail(
        email: String,
        password: String
    ): FlowResource<Unit> = flow {
        try {
            emit(Resource.Loading())

            val credential = EmailAuthProvider.getCredential(email, password)

            auth
                .signInWithCredential(credential)
                .await()

            checkUserDBWorker()

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
    }

    override fun registerWithEmail(
        email: String,
        password: String,
        name: String
    ): FlowResource<Unit> = flow {
        try {
            emit(Resource.Loading())

            auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            auth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
            )?.await()

            checkUserDBWorker()

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
    }

    private suspend fun checkUserDBWorker() {
        val request = OneTimeWorkRequestBuilder<CheckUserDBWorker>().build()
        workManager.enqueue(request).await()
    }

    override fun emailExists(email: String): FlowResource<Boolean> = flow {
        try {
            emit(Resource.Loading())

            val signInMethods = auth.fetchSignInMethodsForEmail(email).await().signInMethods
            val emailInSignInMethods = EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD in signInMethods.orEmpty()

            emit(Resource.Success(emailInSignInMethods))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred"))
        }
    }
}
