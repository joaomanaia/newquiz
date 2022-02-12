package com.infinitepower.newquiz.compose.worker.user

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.data.remote.user.User
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltWorker
class CreateUserDBWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val authUserApi: AuthUserApi,
    private val userApi: UserApi
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val uid = authUserApi.getUid() ?: return Result.failure()

        updateAuthNewUser()

        val authUserNotExists = verifyUserNotExists(uid)
        if (authUserNotExists) createNewUser(uid)

        return Result.success()
    }

    private suspend fun updateAuthNewUser() {
        try {
            authUserApi.updateAuthNewUser()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun verifyUserNotExists(uid: String) = userApi.getUserByUid(uid) == null

    private suspend fun createNewUser(uid: String) {
        val user = User(
            uid = uid,
            info = User.UserInfo(
                fullname = authUserApi.getName(),
                imageUrl = authUserApi.getPhotoUrl()?.toString()
            )
        )

        userApi.createUser(user)
    }
}