package com.infinitepower.newquiz.data.worker.daily_challenge

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.data.database.AppDatabase
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@SmallTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
internal class VerifyDailyChallengeWorkerTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var appDatabase: AppDatabase

    @Inject lateinit var workerFactory: HiltWorkerFactory

    @Inject lateinit var dailyChallengeRepository: DailyChallengeRepository

    private lateinit var context: Context

    @Before
    fun setup() {
        hiltRule.inject()

        context = InstrumentationRegistry.getInstrumentation().context
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun testPeriodicWork() = runTest {
        // Clear the tasks.
        dailyChallengeRepository.resetTasks()

        val verifyDailyChallengeWorker = TestListenableWorkerBuilder<VerifyDailyChallengeWorker>(context)
            .setWorkerFactory(workerFactory)
            .build()

        val result = verifyDailyChallengeWorker.doWork()

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(ListenableWorker.Result.success())

        val tasks = dailyChallengeRepository.getAvailableTasks()
        assertThat(tasks).isNotEmpty()
        assertThat(tasks).hasSize(VerifyDailyChallengeWorker.DEFAULT_TASKS_TO_GENERATE)
    }
}