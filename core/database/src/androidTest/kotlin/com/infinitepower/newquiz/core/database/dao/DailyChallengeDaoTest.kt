package com.infinitepower.newquiz.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.AppDatabase
import com.infinitepower.newquiz.core.database.model.DailyChallengeTaskEntity
import com.infinitepower.newquiz.core.testing.data.fake.FakeData
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.time.Duration.Companion.days

@RunWith(AndroidJUnit4::class)
internal class DailyChallengeDaoTest {
    private lateinit var dailyChallengeDao: DailyChallengeDao
    private lateinit var db: AppDatabase

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dailyChallengeDao = db.dailyChallengeDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAllTasksFlow_returnsFlowOfAllTasks() = runTest {
        val tasks = FakeData.generateTasks().map(DailyChallengeTask::toEntity)
        dailyChallengeDao.insertAll(tasks.map { it.copy(id = 0) })

        dailyChallengeDao.getAllTasksFlow().test {
            val emittedTasks = awaitItem()
            assertThat(emittedTasks).containsExactlyElementsIn(tasks)
        }
    }

    @Test
    fun getTasksForDateRange_returnsTasksWithinDateRange() = runTest {
        val now = Clock.System.now()
        val tasks = FakeData
            .generateTasksWithOffset(instant = now)
            .map(DailyChallengeTask::toEntity)

        dailyChallengeDao.insertAll(tasks.map { it.copy(id = 0) })

        val tasksForDateRange = dailyChallengeDao.getAvailableTasks(now.toEpochMilliseconds())

        tasksForDateRange.forEach {
            assertThat(it.startDate).isAtMost(now.toEpochMilliseconds())
            assertThat(it.endDate).isAtLeast(now.toEpochMilliseconds())
        }
    }

    @Test
    fun tasksAreAvailable_returnsTrueIfTasksAreAvailable() = runTest {
        val now = Clock.System.now()
        val tasks = FakeData
            .generateTasksWithOffset(instant = now)
            .map(DailyChallengeTask::toEntity)

        dailyChallengeDao.insertAll(tasks.map { it.copy(id = 0) })

        val areTasksAvailable = dailyChallengeDao.tasksAreAvailable(now.toEpochMilliseconds())

        assertThat(areTasksAvailable).isTrue()
    }

    @Test
    fun tasksAreAvailable_returnsFalseIfTasksAreNotAvailable() = runTest {
        val now = Clock.System.now()
        val tasks = FakeData
            .generateTasksWithOffset(instant = now)
            .map(DailyChallengeTask::toEntity)

        dailyChallengeDao.insertAll(tasks.map { it.copy(id = 0) })

        val areTasksAvailable = dailyChallengeDao.tasksAreAvailable(
            now.plus(10.days).toEpochMilliseconds()
        )

        assertThat(areTasksAvailable).isFalse()
    }
}

private fun DailyChallengeTask.toEntity(): DailyChallengeTaskEntity = DailyChallengeTaskEntity(
    id = id,
    diamondsReward = diamondsReward.toInt(),
    experienceReward = experienceReward.toInt(),
    isClaimed = isClaimed,
    currentValue = currentValue.toInt(),
    maxValue = maxValue.toInt(),
    type = event.key,
    startDate = dateRange.start.toEpochMilliseconds(),
    endDate = dateRange.endInclusive.toEpochMilliseconds()
)
