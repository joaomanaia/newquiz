package com.infinitepower.newquiz.online_services.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.online_services.domain.user.UserApi
import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import com.infinitepower.newquiz.online_services.model.user.UserEntity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CheckUserDBWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userApi: UserApi,
    private val authUserRepository: AuthUserRepository,
    private val remoteConfig: RemoteConfig
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        fun enqueue(workManager: WorkManager) {
            val checkUserDBWorker = OneTimeWorkRequestBuilder<CheckUserDBWorker>().build()
            workManager.enqueue(checkUserDBWorker)
        }
    }

    override suspend fun doWork(): Result {
        // Check if user is signed in
        val localUid = authUserRepository.uid ?: return Result.failure()

        val localUser = userApi.getUserByUid(localUid)

        // If user exists in database there is no need to create the user.
        if (localUser != null) return Result.success()

        val initialDiamonds = remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS)

        val newUser = UserEntity(
            uid = localUid,
            info = UserEntity.UserInfo(
                fullName = authUserRepository.name ?: "NewQuiz User",
                imageUrl = authUserRepository.photoUrl?.toString()
            ),
            data = UserEntity.UserData(diamonds = initialDiamonds)
        )

        userApi.createUser(newUser)

        return Result.success()
    }
}