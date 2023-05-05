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
        private const val EVENTS_KEY = "events_key"

        fun enqueueWork(
            workManager: WorkManager,
            vararg event: GameEvent
        ): Operation {
            val eventsKey = event.map { it.key }.toTypedArray()

            val workRequest = OneTimeWorkRequestBuilder<UpdateGlobalEventDataWorker>()
                .setInputData(workDataOf(EVENTS_KEY to eventsKey))
                .build()

            return workManager.enqueue(workRequest)
        }
    }

    override suspend fun doWork(): Result {
        val eventsKey = inputData.getStringArray(EVENTS_KEY) ?: return Result.failure()

        // Update daily challenge task data.
        eventsKey.forEach { eventKey ->
            // Get the event from the key.
            try {
                val event = GameEvent.fromKey(eventKey)

                dailyChallengeRepository.completeTaskStep(event)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return Result.success()
    }
}