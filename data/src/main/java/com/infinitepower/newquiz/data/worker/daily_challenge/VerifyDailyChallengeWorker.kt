package com.infinitepower.newquiz.data.worker.daily_challenge

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaDuration

@HiltWorker
class VerifyDailyChallengeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dailyChallengeRepository: DailyChallengeRepository
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        private const val WORK_NAME = "VerifyDailyChallengeWorker"

        const val DEFAULT_TASKS_TO_GENERATE = 5
        const val TASKS_TO_GENERATE_KEY = "tasks_to_generate"

        fun enqueueUniquePeriodicWork(workManager: WorkManager): Operation {
            val verifyDailyChallengeWorker = PeriodicWorkRequestBuilder<VerifyDailyChallengeWorker>(
                repeatInterval = 1.days.toJavaDuration()
            ).build()

            return workManager.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, verifyDailyChallengeWorker)
        }
    }

    override suspend fun doWork(): Result {
        // Get the number of tasks to generate if the tasks are expired or not generated yet.
        val tasksToGenerate = inputData.getInt(TASKS_TO_GENERATE_KEY, DEFAULT_TASKS_TO_GENERATE)

        // Check if the tasks are expired or not generated yet.
        dailyChallengeRepository.checkAndGenerateTasksIfNeeded(tasksToGenerate)

        return Result.success()
    }
}