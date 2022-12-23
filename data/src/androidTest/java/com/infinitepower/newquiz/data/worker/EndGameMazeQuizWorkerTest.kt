package com.infinitepower.newquiz.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.data.database.AppDatabase
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
internal class EndGameMazeQuizWorkerTest {

    private lateinit var context: Context

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var workerFactory: HiltWorkerFactory

    @Inject lateinit var appDatabase: AppDatabase

    @Inject lateinit var mazeQuizDao: MazeQuizDao

    @Inject lateinit var mazeMathQuizRepository: MazeQuizRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        hiltRule.inject()
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    fun testDoWork_success() = runTest {
        mazeQuizDao.deleteAll()
        val randomMazeItems = mazeMathQuizRepository.generateMaze()
        mazeQuizDao.insertItems(randomMazeItems.items)

        val firstSavedItem = mazeQuizDao.getAllMazeItems().first()

        // Arrange
        val mazeItemId = firstSavedItem.id

        val inputData = workDataOf(EndGameMazeQuizWorker.INPUT_MAZE_ITEM_ID to mazeItemId)

        val worker = TestListenableWorkerBuilder<EndGameMazeQuizWorker>(context)
            .setWorkerFactory(workerFactory)
            .setInputData(inputData)
            .build()

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        val result = worker.doWork()

        assertThat(result).isEqualTo(ListenableWorker.Result.success())

        val updatedMazeItem1 = mazeQuizDao.getMazeItemById(mazeItemId)

        assertThat(updatedMazeItem1).isNotNull()
        assertThat(updatedMazeItem1?.played).isTrue()

        val secondItem = mazeQuizDao.getAllMazeItems()[1]
        assertThat(secondItem.played).isFalse()
    }
}