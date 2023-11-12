package com.infinitepower.newquiz.data.worker.maze

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.dao.MazeQuizDao
import com.infinitepower.newquiz.core.database.model.MazeQuizItemEntity
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.BeforeTest
import kotlin.test.Test

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class CleanMazeQuizWorkerTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var workerFactory: HiltWorkerFactory

    @Inject lateinit var mazeQuizDao: MazeQuizDao

    private lateinit var context: Context

    @BeforeTest
    fun setup() {
        hiltRule.inject()

        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testCleanMazeQuizWorker() = runTest {
        val mazeQuizItems = List(10) {
            MazeQuizItemEntity(
                difficulty = QuestionDifficulty.Easy,
                played = false,
                type = MazeQuizItemEntity.Type.MULTI_CHOICE,
                mazeSeed = 0
            )
        }

        mazeQuizDao.insertItems(mazeQuizItems)

        // Check if the items were inserted correctly.
        val mazeQuizItemsBeforeClean = mazeQuizDao.getAllMazeItems()
        assertThat(mazeQuizItemsBeforeClean).hasSize(10)

        // Clean the maze quiz items.
        val cleanSavedMazeRequest = TestListenableWorkerBuilder<CleanMazeQuizWorker>(context)
            .setWorkerFactory(workerFactory)
            .build()

        val result = cleanSavedMazeRequest.doWork()

        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(ListenableWorker.Result.success())

        // Check if the items were deleted correctly.
        val mazeQuizItemsAfterClean = mazeQuizDao.getAllMazeItems()
        assertThat(mazeQuizItemsAfterClean).isEmpty()
    }
}
