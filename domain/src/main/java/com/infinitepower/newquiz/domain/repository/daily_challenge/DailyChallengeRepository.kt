package com.infinitepower.newquiz.domain.repository.daily_challenge

import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import com.infinitepower.newquiz.model.global_event.GameEvent
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

/**
 * Daily challenge repository interface
 */
interface DailyChallengeRepository {
    /**
     * Returns all the daily challenge tasks
     */
    fun getAvailableTasksFlow(): Flow<List<DailyChallengeTask>>

    suspend fun getAllTasks(): List<DailyChallengeTask>

    suspend fun getAvailableTasks(): List<DailyChallengeTask>

    /**
     * Checks if the daily tasks are expired and generates new ones if needed.
     * Will be generated unique types of tasks.
     *
     * @param tasksToGenerate The number of tasks to generate
     */
    suspend fun checkAndGenerateTasksIfNeeded(
        tasksToGenerate: Int = 5,
        random: Random = Random
    )

    /**
     * Completes the task step if it exists.
     * If the task is not found, nothing happens.
     */
    suspend fun completeTaskStep(taskType: GameEvent)

    suspend fun claimTask(taskType: GameEvent)

    suspend fun resetTasks()
}
