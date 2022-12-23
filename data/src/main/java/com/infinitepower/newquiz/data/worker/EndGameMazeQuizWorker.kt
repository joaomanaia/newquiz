package com.infinitepower.newquiz.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Runs when the user completes (with correct answer) the maze item.
 */
@HiltWorker
class EndGameMazeQuizWorker  @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val mazeQuizDao: MazeQuizDao
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val INPUT_MAZE_ITEM_ID = "INPUT_MAZE_ITEM_ID"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val mazeItemId = inputData.getInt(INPUT_MAZE_ITEM_ID, -1)
        if (mazeItemId == -1) return@withContext Result.failure()

        val allMazeItems = mazeQuizDao.getAllMazeItems()

        val mazeItem = allMazeItems.find { item ->
            item.id == mazeItemId
        } ?: return@withContext Result.failure()

        val updatedMazeItem = mazeItem.copy(played = true)

        mazeQuizDao.updateItem(updatedMazeItem)

        return@withContext Result.success()
    }
}