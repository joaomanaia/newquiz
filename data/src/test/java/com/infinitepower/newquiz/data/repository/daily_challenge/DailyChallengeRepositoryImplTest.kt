package com.infinitepower.newquiz.data.repository.daily_challenge

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.testing.data.fake.FakeComparisonQuizData
import com.infinitepower.newquiz.core.testing.data.fake.FakeData
import com.infinitepower.newquiz.core.testing.domain.FakeDailyChallengeDao
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.data.util.mappers.daily_challenge.toEntity
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import com.infinitepower.newquiz.model.global_event.GameEvent
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.jupiter.api.assertThrows
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.days

internal class DailyChallengeRepositoryImplTest {
    private val dailyChallengeDao = FakeDailyChallengeDao()
    private val comparisonQuizRepository = mockk<ComparisonQuizRepository>()
    private val remoteConfig = mockk<RemoteConfig>()
    private val userService = mockk<UserService>()

    private lateinit var dailyChallengeRepository: DailyChallengeRepositoryImpl

    @BeforeTest
    fun setup() {
        val comparisonQuizCategories = FakeComparisonQuizData.generateCategories()
        coEvery { comparisonQuizRepository.getCategories() } returns comparisonQuizCategories

        dailyChallengeRepository = DailyChallengeRepositoryImpl(
            dailyChallengeDao = dailyChallengeDao,
            comparisonQuizRepository = comparisonQuizRepository,
            remoteConfig = remoteConfig,
            userService = userService
        )
    }

    @AfterTest
    fun tearDown() {
        clearAllMocks()
        runTest {
            dailyChallengeDao.deleteAll()
        }
    }

    @Test
    fun `getAllTasks returns all tasks`() = runTest {
        val tasks = FakeData.generateTasks()
        dailyChallengeDao.insertAll(tasks.map(DailyChallengeTask::toEntity))

        val resultTasks = dailyChallengeRepository.getAllTasks()

        assertThat(resultTasks).containsExactlyElementsIn(tasks)

        coVerify(exactly = 1) { comparisonQuizRepository.getCategories() }
        confirmVerified()
    }

    @Test
    fun `getAvailableTasks returns available tasks`() = runTest {
        val tasks = FakeData.generateTasks()
        val expiredTasks = FakeData.generateTasks(
            count = 5,
            dayDuration = (-2).days..(-1).days
        )
        val newTasks = FakeData.generateTasks(
            count = 5,
            dayDuration = (1).days..10.days
        )
        val allTasks = tasks + expiredTasks + newTasks
        dailyChallengeDao.insertAll(allTasks.map(DailyChallengeTask::toEntity))

        val resultTasks = dailyChallengeRepository.getAvailableTasks()

        assertThat(resultTasks).containsExactlyElementsIn(tasks)

        coVerify(exactly = 1) { comparisonQuizRepository.getCategories() }
        confirmVerified()
    }

    @Test
    fun `getAvailableTasksFlow returns available tasks`() = runTest {
        val tasks = FakeData.generateTasks()
        val expiredTasks = FakeData.generateTasks(
            count = 5,
            dayDuration = (-2).days..(-1).days
        )
        val newTasks = FakeData.generateTasks(
            count = 5,
            dayDuration = (1).days..10.days
        )
        val allTasks = tasks + expiredTasks + newTasks

        dailyChallengeRepository.getAvailableTasksFlow().test {
            dailyChallengeDao.insertAll(allTasks.map(DailyChallengeTask::toEntity))

            assertThat(awaitItem()).isEmpty()
            assertThat(awaitItem()).containsExactlyElementsIn(tasks)
        }

        coVerify(exactly = 1) { comparisonQuizRepository.getCategories() }
        confirmVerified()
    }

    @Test
    fun `getClaimableTasksCountFlow returns correct count`() = runTest {
        val tasks = FakeData.generateTasks().toMutableList().apply {
            set(0, get(0).copy(currentValue = 10u, maxValue = 10u))
        }

        dailyChallengeRepository.getClaimableTasksCountFlow().test {
            dailyChallengeDao.insertAll(tasks.map(DailyChallengeTask::toEntity))

            assertThat(awaitItem()).isEqualTo(0)
            assertThat(awaitItem()).isEqualTo(tasks.count { it.isClaimable() })
        }

        coVerify(exactly = 1) { comparisonQuizRepository.getCategories() }
        confirmVerified()
    }

    @Test
    fun `resetTasks deletes all tasks`() = runTest {
        val tasks = FakeData.generateTasks()
        dailyChallengeDao.insertAll(tasks.map(DailyChallengeTask::toEntity))

        dailyChallengeRepository.resetTasks()

        confirmVerified() // No calls should be made to the mock

        // Verify that the tasks are deleted
        assertThat(dailyChallengeRepository.getAllTasks()).isEmpty()
    }

    @Test
    fun `checkAndGenerateTasksIfNeeded does not generate tasks if they are not expired`() =
        runTest {
            val tasks = FakeData.generateTasks()
            dailyChallengeDao.insertAll(tasks.map(DailyChallengeTask::toEntity))

            dailyChallengeRepository.checkAndGenerateTasksIfNeeded(tasksToGenerate = 10)

            coVerify(exactly = 0) {
                comparisonQuizRepository.getCategories()
                remoteConfig.get(RemoteConfigValue.DAILY_CHALLENGE_ITEM_REWARD)
            }
            confirmVerified()

            // Verify that the tasks are not changed
            assertThat(dailyChallengeRepository.getAllTasks()).containsExactlyElementsIn(tasks)
        }

    @Test
    fun `checkAndGenerateTasksIfNeeded generates tasks if they are expired`() = runTest {
        val tasksToGenerate = 10
        val diamondsReward = 10

        val expiredTasks = FakeData.generateTasks(
            count = 5,
            dayDuration = (-2).days..(-1).days // 2 days ago
        )
        dailyChallengeDao.insertAll(expiredTasks.map(DailyChallengeTask::toEntity))

        every { remoteConfig.get(RemoteConfigValue.DAILY_CHALLENGE_ITEM_REWARD) } returns diamondsReward

        dailyChallengeRepository.checkAndGenerateTasksIfNeeded(tasksToGenerate = tasksToGenerate)

        coVerify(exactly = 1) {
            comparisonQuizRepository.getCategories()
            remoteConfig.get(RemoteConfigValue.DAILY_CHALLENGE_ITEM_REWARD)
        }
        confirmVerified()

        // Check if the tasks are generated
        dailyChallengeRepository
            .getAvailableTasks()
            .also {
                assertThat(it).hasSize(tasksToGenerate)
            }.forEach {
                assertThat(it.isClaimed).isFalse()
                assertThat(it.currentValue).isEqualTo(0u)
                assertThat(it.isClaimed).isFalse()

                val now = Clock.System.now()
                assertThat(now).isAtLeast(it.dateRange.start)
                assertThat(now).isAtMost(it.dateRange.endInclusive)
            }
    }

    @Test
    fun `completeTaskStep throws exception if task is not found`() = runTest {
        assertThrows<NullPointerException> {
            dailyChallengeRepository.completeTaskStep(GameEvent.MultiChoice.PlayQuestions)
        }
    }

    @Test
    fun `completeTaskStep throws exception if task is expired`() = runTest {
        val tasks = FakeData.generateTasks(
            count = 5,
            dayDuration = (-2).days..(-1).days
        )
        dailyChallengeDao.insertAll(tasks.map(DailyChallengeTask::toEntity))

        assertThrows<IllegalStateException> {
            dailyChallengeRepository.completeTaskStep(tasks.first().event)
        }
    }

    @Test
    fun `completeTaskStep throws exception if task is already claimed`() = runTest {
        val now = Clock.System.now()
        val dateRange = now..now + 1.days
        val task = FakeData
            .generateTask(id = 1, dateRange = dateRange)
            .copy(isClaimed = true)

        dailyChallengeDao.insertAll(task.toEntity())

        assertThrows<IllegalStateException> {
            dailyChallengeRepository.completeTaskStep(task.event)
        }
    }

    @Test
    fun `completeTaskStep updates the current value if exists and is available`() = runTest {
        val now = Clock.System.now()
        val dateRange = now..now + 1.days
        val task = FakeData.generateTask(id = 1, dateRange = dateRange)

        assertThat(task.currentValue).isEqualTo(0u)

        dailyChallengeDao.insertAll(task.toEntity())

        dailyChallengeRepository.completeTaskStep(task.event)

        val updatedTask = dailyChallengeDao.getTaskByType(task.event.key)!!
        assertThat(updatedTask.currentValue).isEqualTo(1)
    }

    @Test
    fun `claimTask throws exception if task is not found`() = runTest {
        assertThrows<NullPointerException> {
            dailyChallengeRepository.claimTask(GameEvent.MultiChoice.PlayQuestions)
        }
    }

    @Test
    fun `claimTask throws exception if task is expired`() = runTest {
        val tasks = FakeData.generateTasks(
            count = 5,
            dayDuration = (-2).days..(-1).days
        )
        dailyChallengeDao.insertAll(tasks.map(DailyChallengeTask::toEntity))

        assertThrows<IllegalStateException> {
            dailyChallengeRepository.claimTask(tasks.first().event)
        }
    }

    @Test
    fun `claimTask throws exception if task is already claimed`() = runTest {
        val now = Clock.System.now()
        val dateRange = now..now + 1.days
        val task = FakeData
            .generateTask(id = 1, dateRange = dateRange)
            .copy(isClaimed = true)

        dailyChallengeDao.insertAll(task.toEntity())

        assertThrows<IllegalStateException> {
            dailyChallengeRepository.claimTask(task.event)
        }
    }

    @Test
    fun `claimTask throws exception if task is not completed`() = runTest {
        val now = Clock.System.now()
        val dateRange = now..now + 1.days
        val task = FakeData.generateTask(id = 1, dateRange = dateRange)
            .copy(currentValue = 5u, maxValue = 10u)

        dailyChallengeDao.insertAll(task.toEntity())

        assertThrows<IllegalStateException> {
            dailyChallengeRepository.claimTask(task.event)
        }
        confirmVerified()
    }

    @Test
    fun `claimTask updates the task if exists and is available`() = runTest {
        val now = Clock.System.now()
        val dateRange = now..now + 1.days
        val task = FakeData
            .generateTask(id = 1, dateRange = dateRange)
            .copy(currentValue = 10u, maxValue = 10u)

        assertThat(task.isClaimable()).isTrue()
        assertThat(task.isClaimed).isFalse()

        coJustRun { userService.addRemoveDiamonds(task.diamondsReward.toInt()) }

        dailyChallengeDao.insertAll(task.toEntity())

        dailyChallengeRepository.claimTask(task.event)

        val updatedTask = dailyChallengeDao.getTaskByType(task.event.key)!!
        assertThat(updatedTask.isClaimed).isTrue()

        coVerify(exactly = 1) {
            userService.addRemoveDiamonds(task.diamondsReward.toInt())
        }
        confirmVerified()
    }
}
