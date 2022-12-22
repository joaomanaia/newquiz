package com.infinitepower.newquiz.data.repository.math_quiz.maze

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.math_quiz.maze.MazeMathQuizRepository
import com.infinitepower.newquiz.domain.repository.math_quiz.maze.MazeQuizDao
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.math_quiz.maze.MathQuizMaze
import com.infinitepower.newquiz.model.math_quiz.maze.MazePoint
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MazeMathQuizRepositoryImpl @Inject constructor(
    private val mathQuizCoreRepository: MathQuizCoreRepository,
    private val mazeQuizDao: MazeQuizDao
) : MazeMathQuizRepository {
    override suspend fun generateMaze(
        questionSize: Int,
        operatorSize: Int,
        difficulty: QuestionDifficulty,
        random: Random
    ): MathQuizMaze {
        val formulas = generateRandomFormulas(
            questionSize = questionSize,
            operatorSize = operatorSize,
            difficulty = difficulty,
            random = random
        ).map { formula ->
            MathQuizMaze.MazeItem(formula = formula, difficulty = difficulty)
        }

        return MathQuizMaze(formulas = formulas)
    }

    private fun generateRandomFormulas(
        questionSize: Int,
        operatorSize: Int = 1,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): List<MathFormula> {
        val formulas = mutableListOf<MathFormula>()

        while (formulas.size < questionSize) {
            val generatedFormula = mathQuizCoreRepository.generateMathFormula(
                operatorSize = operatorSize,
                difficulty = difficulty,
                random = random
            )

            if (generatedFormula in formulas) continue

            formulas.add(generatedFormula)
        }

        return formulas
    }

    override suspend fun saveMaze(maze: MathQuizMaze) {
        maze.formulas.forEach { item ->
            mazeQuizDao.updateItem(item)
        }
    }

    override fun getSavedMazeQuizFlow(): FlowResource<MathQuizMaze> = flow {
        try {
            emit(Resource.Loading())

            val mazeFlow = mazeQuizDao
                .getAllMazeItemsFlow()
                .map { mazeItems ->
                    val maze = MathQuizMaze(formulas = mazeItems)
                    Resource.Success(maze)
                }

            emitAll(mazeFlow)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading saved maze quiz"))
        }
    }
}