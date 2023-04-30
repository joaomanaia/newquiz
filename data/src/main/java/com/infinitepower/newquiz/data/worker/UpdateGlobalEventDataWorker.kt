package com.infinitepower.newquiz.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.model.global_event.GameEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * This worker is responsible for updating the game event data.
 *
 * Updates daily challenge task data.
 * Update achievements data.
 */
@HiltWorker
class UpdateGlobalEventDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dailyChallengeRepository: DailyChallengeRepository
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        private const val EVENT_KEY = "event_key"

        fun enqueueWork(
            workManager: WorkManager,
            event: GameEvent
        ): Operation {
            val workRequest = OneTimeWorkRequestBuilder<UpdateGlobalEventDataWorker>()
                .setInputData(
                    workDataOf(
                        EVENT_KEY to event.key
                    )
                ).build()

            return workManager.enqueue(workRequest)
        }
    }

    override suspend fun doWork(): Result {
        val eventKey = inputData.getString(EVENT_KEY) ?: return Result.failure()

        // Get the event from the key.
        val event = GameEvent.fromKey(eventKey)

        // Update daily challenge task data.
        try {
            dailyChallengeRepository.completeTaskStep(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Result.success()
    }
}