package com.infinitepower.newquiz.data.repository.daily_challenge

import android.util.Log
import com.infinitepower.newquiz.core.database.dao.DailyChallengeDao
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.data.util.mappers.toEntity
import com.infinitepower.newquiz.data.util.mappers.toModel
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.repository.daily_challenge.util.getTitle
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

@Singleton
class DailyChallengeRepositoryImpl @Inject constructor(
    private val dailyChallengeDao: DailyChallengeDao,
    private val comparisonQuizRepository: ComparisonQuizRepository,
    private val remoteConfig: RemoteConfig,
    private val userRepository: UserRepository
) : DailyChallengeRepository {
    override fun getAvailableTasksFlow(): Flow<List<DailyChallengeTask>> = dailyChallengeDao
        .getAllTasksFlow()
        .map { tasks ->
            val now = Clock.System.now().toEpochMilliseconds()

            tasks
                .filter { task -> task.startDate <= now && task.endDate >= now }
                .map { task -> task.toModel(comparisonQuizRepository.getCategories()) }
        }.catch { e -> e.printStackTrace() }

    override suspend fun getAllTasks(): List<DailyChallengeTask> = dailyChallengeDao
        .getAllTasks()
        .map { task -> task.toModel(comparisonQuizRepository.getCategories()) }

    override suspend fun getAvailableTasks(): List<DailyChallengeTask> {
        val now = Clock.System.now().toEpochMilliseconds()

        return dailyChallengeDao
            .getAllTasks()
            .filter { task -> task.startDate <= now && task.endDate >= now }
            .map { task -> task.toModel(comparisonQuizRepository.getCategories()) }
    }

    override fun getClaimableTasksCountFlow(): Flow<Int> = getAvailableTasksFlow().map { tasks ->
        tasks.count { task -> task.isClaimable() }
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
        val dateRange = now.rangeTo(now + 1.days)

        val comparisonQuizCategories = comparisonQuizRepository.getCategories()

        val types = GameEvent.getRandomEvents(
            count = tasksToGenerate,
            multiChoiceCategories = multiChoiceQuestionCategories,
            comparisonQuizCategories = comparisonQuizCategories,
            random = random
        )

        val diamondsReward = remoteConfig.getInt("daily_challenge_item_reward").toUInt()

        val newTasks = types.map { type ->
            val maxValue = type.valueRange.toList().random(random)

            DailyChallengeTask(
                id = random.nextInt(),
                diamondsReward = diamondsReward,
                experienceReward = (10u..100u).random(random),
                isClaimed = false,
                dateRange = dateRange,
                currentValue = 0u,
                maxValue = maxValue,
                event = type,
                title = type.getTitle(maxValue.toInt(), comparisonQuizCategories)
            )
        }.map(DailyChallengeTask::toEntity)

        dailyChallengeDao.insertAll(newTasks)
    }

    private suspend fun checkIfDailyTasksAreExpired(): Boolean {
        return getAvailableTasks().all(DailyChallengeTask::isExpired)
    }

    override suspend fun completeTaskStep(taskType: GameEvent) {
        Log.d("Test", "completeTaskStep: ${taskType.key}")
        val task = dailyChallengeDao.getTaskByType(taskType.key)
            ?.toModel(comparisonQuizRepository.getCategories())
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

        // TODO: Add experience reward
        userRepository.addLocalUserDiamonds(task.diamondsReward.toInt())
    }

    override suspend fun claimTask(taskType: GameEvent) {
        val task = dailyChallengeDao.getTaskByType(taskType.key)
            ?.toModel(comparisonQuizRepository.getCategories())
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
