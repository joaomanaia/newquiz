package com.infinitepower.newquiz.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.domain.repository.math_quiz.maze.MazeQuizDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class EndGameMazeQuizWorker  @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val mazeQuizDao: MazeQuizDao
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val INPUT_MAZE_ITEM_ID = "INPUT_MAZE_ITEM_ID"
        const val INPUT_IS_CORRECT = "INPUT_IS_CORRECT"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val mazeItemId = inputData.getInt(INPUT_MAZE_ITEM_ID, -1)
        if (mazeItemId == -1) return@withContext Result.failure()

        val isQuestionCorrect = inputData.getBoolean(INPUT_IS_CORRECT, false)

        val allMazeItems = mazeQuizDao.getAllMazeItems()

        val mazeItem = allMazeItems.find { item ->
            item.id == mazeItemId
        } ?: return@withContext Result.failure()

        val updatedMazeItem = mazeItem.copy(
            played = true,
            correct = isQuestionCorrect
        )

        mazeQuizDao.updateItem(updatedMazeItem)

        return@withContext Result.success()
    }
}