package com.infinitepower.newquiz.data.worker.math_quiz.maze

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.domain.repository.math_quiz.maze.MazeMathQuizRepository
import com.infinitepower.newquiz.domain.repository.math_quiz.maze.MazeQuizDao
import com.infinitepower.newquiz.model.math_quiz.maze.MazePoint
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

@HiltWorker
class GenerateMathQuizWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val mazeMathQuizRepository: MazeMathQuizRepository,
    private val mazeQuizDao: MazeQuizDao
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val SEED_INPUT = "SEED_INPUT"
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val seed = inputData.getInt(SEED_INPUT, Random.nextInt())
        val random = Random(seed)

        val maze = mazeMathQuizRepository.generateMaze(random = random)
        mazeQuizDao.insertItems(maze.formulas)

        val questionsCount = mazeQuizDao.countAllItems()

        Log.d("MazeQuiz", "Generated questions: ${maze.formulas.count()}, Saved Questions: $questionsCount")

        if (questionsCount != maze.formulas.count())
            throw RuntimeException("Maze saved questions: $questionsCount is not equal to generated questions: ${maze.formulas.count()}")

        Result.success()
    }
}