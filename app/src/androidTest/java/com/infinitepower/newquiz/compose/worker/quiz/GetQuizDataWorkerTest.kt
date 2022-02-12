package com.infinitepower.newquiz.compose.worker.quiz

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.testing.WorkManagerTestInitHelper
import androidx.work.workDataOf
import com.infinitepower.newquiz.compose.MainActivity
import com.infinitepower.newquiz.compose.core.worker.rule.WorkManagerRule
import com.infinitepower.newquiz.compose.di.AppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(AppModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GetQuizDataWorkerTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val workManagerRule = WorkManagerRule()
    
    @get:Rule(order = 2)
    val activityRule =  ActivityScenarioRule(MainActivity::class.java)

    private lateinit var context: Context

    @BeforeTest
    fun setup() {
        hiltRule.inject()

        context = ApplicationProvider.getApplicationContext<Context>()

        /*
        context = ApplicationProvider.getApplicationContext()
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .setWorkerFactory(workerFactory)
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        
         */
    }

    @Test
    fun testGetTestQuestions() = runTest {
        val inputData = workDataOf(
            GetQuizDataWorker.TEST_QUESTIONS_ENABLED_PARAM to true
        )

        val worker = TestListenableWorkerBuilder<GetQuizDataWorker>(context)
            .setInputData(inputData)
            .build()

        val result = worker.doWork()
        assertTrue(result is ListenableWorker.Result.Success)
    }
}