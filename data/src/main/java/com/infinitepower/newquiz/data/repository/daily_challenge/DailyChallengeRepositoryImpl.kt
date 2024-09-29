package com.infinitepower.newquiz.data.repository.daily_challenge

import com.infinitepower.newquiz.core.database.dao.DailyChallengeDao
import com.infinitepower.newquiz.core.database.model.DailyChallengeTaskEntity
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.util.mappers.toModel
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import com.infinitepower.newquiz.model.global_event.GameEvent
import kotlinx.coroutines.flow.Flow
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
    private val userService: UserService
) : DailyChallengeRepository {
    override fun getAvailableTasksFlow(): Flow<List<DailyChallengeTask>> {
        val comparisonQuizCategories = comparisonQuizRepository.getCategories()

        return dailyChallengeDao
            .getAllTasksFlow()
            .map { tasks ->
                val now = Clock.System.now().toEpochMilliseconds()

                tasks
                    .filter { task -> task.startDate <= now && task.endDate >= now }
                    .map { task -> task.toModel(comparisonQuizCategories) }
            }
    }

    override suspend fun getAllTasks(): List<DailyChallengeTask> {
        val comparisonQuizCategories = comparisonQuizRepository.getCategories()

        return dailyChallengeDao
            .getAllTasks()
            .map { task -> task.toModel(comparisonQuizCategories) }
    }

    override suspend fun getAvailableTasks(): List<DailyChallengeTask> {
        val comparisonQuizCategories = comparisonQuizRepository.getCategories()
        val now = Clock.System.now().toEpochMilliseconds()

        return dailyChallengeDao
            .getAllTasks()
            .filter { task -> now in task.startDate..task.endDate }
            .map { task -> task.toModel(comparisonQuizCategories) }
    }

    override fun getClaimableTasksCountFlow(): Flow<Int> = getAvailableTasksFlow().map { tasks ->
        tasks.count { task -> task.isClaimable() }
    }

    override suspend fun checkAndGenerateTasksIfNeeded(
        tasksToGenerate: Int,
        random: Random
    ) {
        val now = Clock.System.now().toEpochMilliseconds()
        val tasksAreExpired = !dailyChallengeDao.tasksAreAvailable(now)

        // Check if the daily tasks are expired
        if (tasksAreExpired) {
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

        val types = GameEvent.getRandomEvents(
            count = tasksToGenerate,
            multiChoiceCategories = multiChoiceQuestionCategories,
            comparisonQuizCategories = comparisonQuizRepository.getCategories(),
            random = random
        )

        val diamondsReward =
            remoteConfig.get(RemoteConfigValue.DAILY_CHALLENGE_ITEM_REWARD).toUInt()

        val newTasks = types.map { type ->
            val maxValue = type.valueRange.toList().random(random)

            DailyChallengeTaskEntity(
                id = random.nextInt(),
                diamondsReward = diamondsReward.toInt(),
                experienceReward = (10..100).random(random),
                isClaimed = false,
                currentValue = 0,
                maxValue = maxValue.toInt(),
                type = type.key,
                startDate = dateRange.start.toEpochMilliseconds(),
                endDate = dateRange.endInclusive.toEpochMilliseconds()
            )
        }

        dailyChallengeDao.insertAll(newTasks)
    }

    override suspend fun completeTaskStep(taskType: GameEvent) {
        val task = dailyChallengeDao.getTaskByType(taskType.key)
            ?: throw NullPointerException("Task not found.")

        val now = Clock.System.now()

        // Check if the task is expired
        val taskIsAvailable = now.toEpochMilliseconds() in task.startDate..task.endDate
        check(taskIsAvailable) { "Task (${task.id}) is expired." }

        // Check if the task is already claimed
        check(!task.isClaimed) { "Task (${task.id}) is already claimed." }

        // Update the task
        val newTask = task.copy(currentValue = task.currentValue + 1)

        // Update the tasks set
        dailyChallengeDao.update(newTask)
    }

    override suspend fun claimTask(taskType: GameEvent) {
        val task = dailyChallengeDao.getTaskByType(taskType.key)
            ?: throw NullPointerException("Task not found.")

        val now = Clock.System.now()

        // Check if the task is expired
        val taskIsAvailable = now.toEpochMilliseconds() in task.startDate..task.endDate
        check(taskIsAvailable) { "Task (${task.id}) is expired." }

        // Check if the task is already claimed
        check(!task.isClaimed) { "Task (${task.id}) is already claimed." }

        check(task.currentValue >= task.maxValue) {
            "Task (${task.id}) is not completed."
        }

        // Update the task
        val newTask = task.copy(isClaimed = true)

        // Update the tasks set
        dailyChallengeDao.update(newTask)

        // Give the user the reward
        userService.addRemoveDiamonds(newTask.diamondsReward)
    }

    override suspend fun resetTasks() {
        dailyChallengeDao.deleteAll()
    }
}
