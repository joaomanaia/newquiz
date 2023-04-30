package com.infinitepower.newquiz.data.repository.daily_challenge

import android.content.Context
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.repository.daily_challenge.util.getTitle
import com.infinitepower.newquiz.data.util.mappers.toDomain
import com.infinitepower.newquiz.data.util.mappers.toEntity
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeDao
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import com.infinitepower.newquiz.model.global_event.GameEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

@Singleton
class DailyChallengeRepositoryImpl @Inject constructor(
    private val dailyChallengeDao: DailyChallengeDao,
    @ApplicationContext private val context: Context
) : DailyChallengeRepository {
    override fun getAvailableTasksFlow(): Flow<List<DailyChallengeTask>> = dailyChallengeDao
        .getAllTasksFlow()
        .map { tasks ->
            val now = Clock.System.now().toEpochMilliseconds()

            tasks
                .filter { task -> task.startDate <= now && task.endDate >= now }
                .map { entity -> entity.toDomain(context) }
        }.catch { e -> e.printStackTrace() }

    override suspend fun getAllTasks(): List<DailyChallengeTask> = dailyChallengeDao
        .getAllTasks()
        .map { entity -> entity.toDomain(context) }

    override suspend fun getAvailableTasks(): List<DailyChallengeTask> {
        val now = Clock.System.now().toEpochMilliseconds()

        return dailyChallengeDao
            .getAllTasks()
            .filter { task -> task.startDate <= now && task.endDate >= now }
            .map { entity -> entity.toDomain(context) }
    }

    override suspend fun checkAndGenerateTasksIfNeeded(
        tasksToGenerate: Int,
        random: Random
    ) {
        // Check if the daily tasks are expired
        if (checkIfDailyTasksAreExpired()) {
            generateDailyTasks(
                tasksToGenerate = tasksToGenerate,
                random = random
            )
        }
    }

    private suspend fun generateDailyTasks(
        tasksToGenerate: Int,
        random: Random = Random
    ) {
        val now = Clock.System.now()

        val types = GameEvent.getRandomEvents(
            count = tasksToGenerate,
            multiChoiceCategories = multiChoiceQuestionCategories,
            random = random
        )

        val newTasks = types.map { type ->
            val maxValue = type.valueRange.toList().random()

            createDailyChallengeTask(
                context = context,
                maxValue = maxValue,
                type = type,
                now = now,
                random = random
            )
        }.map(DailyChallengeTask::toEntity)

        dailyChallengeDao.insertAll(newTasks)
    }

    private suspend fun checkIfDailyTasksAreExpired(): Boolean {
        return getAvailableTasks().all(DailyChallengeTask::isExpired)
    }

    private suspend fun checkIfWeeklyTaskIsExpired(): Boolean {
        val weeklyTask = getAvailableTasks().filter(DailyChallengeTask::isWeekly)

        return weeklyTask.all(DailyChallengeTask::isExpired)
    }

    override suspend fun completeTaskStep(taskType: GameEvent) {
        val task = dailyChallengeDao.getTaskByType(taskType.key)?.toDomain(context)
            ?: throw NullPointerException("Task not found.")

        // Check if the task is expired
        if (task.isExpired()) {
            throw IllegalStateException("Task (${task.title}) is expired.")
        }

        // Check if the task is already claimed
        if (task.isClaimed) {
            throw IllegalStateException("Task (${task.title}) is already claimed.")
        }

        // Update the task
        val newTask = task.copy(currentValue = task.currentValue + 1u)

        val newTaskEntity = newTask.toEntity()

        // Update the tasks set
        dailyChallengeDao.update(newTaskEntity)
    }

    override suspend fun claimTask(taskType: GameEvent) {
        val task = dailyChallengeDao.getTaskByType(taskType.key)?.toDomain(context)
            ?: throw NullPointerException("Task not found.")

        // Check if the task is expired
        if (task.isExpired()) {
            throw IllegalStateException("Task (${task.title}) is expired.")
        }

        // Check if the task is already claimed
        if (task.isClaimed) {
            throw IllegalStateException("Task (${task.title}) is already claimed.")
        }

        // Update the task
        val newTask = task.copy(isClaimed = true)

        val newTaskEntity = newTask.toEntity()

        // Update the tasks set
        dailyChallengeDao.update(newTaskEntity)
    }

    override suspend fun resetTasks() {
        dailyChallengeDao.deleteAll()
    }
}

internal fun createDailyChallengeTask(
    context: Context,
    maxValue: UInt = 10u,
    type: GameEvent,
    now: Instant = Clock.System.now(),
    dateRange: ClosedRange<Instant> = (now - 1.days).rangeTo(now + 1.days),
    random: Random = Random
): DailyChallengeTask = DailyChallengeTask(
    id = random.nextInt(),
    diamondsReward = 1u,
    experienceReward = 1u,
    isClaimed = false,
    dateRange = dateRange,
    currentValue = 0u,
    maxValue = maxValue,
    event = type,
    title = type.getTitle(maxValue.toInt(), context)
)
