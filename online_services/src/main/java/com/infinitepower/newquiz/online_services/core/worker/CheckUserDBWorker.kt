package com.infinitepower.newquiz.online_services.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.online_services.model.user.User
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CheckUserDBWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userRepository: UserRepository,
    private val authUserRepository: AuthUserRepository
) : CoroutineWorker(appContext, workerParams) {
    private val remoteConfig by lazy { Firebase.remoteConfig }

    override suspend fun doWork(): Result {
        // Check if user is signed in
        val localUid = authUserRepository.uid ?: return Result.failure()

        val localUser = userRepository.getUserByUid(localUid)

        // If user exists in database there is no need to create the user.
        if (localUser != null) return Result.success()

        val initialDiamonds = remoteConfig.getLong("user_initial_diamonds")

        val newUser = User(
            uid = localUid,
            info = User.UserInfo(
                fullName = authUserRepository.name,
                imageUrl = authUserRepository.photoUrl?.toString()
            ),
            data = User.UserData(diamonds = initialDiamonds.toInt())
        )

        userRepository.createUserDB(newUser)

        return Result.success()
    }
}