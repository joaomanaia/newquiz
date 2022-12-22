package com.infinitepower.newquiz.data.local.math_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.math.evaluator.Expressions
import com.infinitepower.newquiz.data.repository.math_quiz.maze.MathQuizCoreRepositoryImpl
import com.infinitepower.newquiz.data.repository.math_quiz.maze.MazeMathQuizRepositoryImpl
import com.infinitepower.newquiz.domain.repository.math_quiz.maze.MazeMathQuizRepository
import com.infinitepower.newquiz.domain.repository.math_quiz.maze.MazeQuizDao
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.math_quiz.maze.MathQuizMaze
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class MazeMathQuizRepositoryImplTest {
    private val mazeQuizDaoMockk = mockk<MazeQuizDao>()

    private lateinit var mazeMathQuizRepository: MazeMathQuizRepository

    @BeforeEach
    fun setup() {
        val expressions = Expressions()
        val mathQuizCoreRepository = MathQuizCoreRepositoryImpl(expressions)

        every {
            mazeQuizDaoMockk.getAllMazeItemsFlow()
        } returns flowOf(
            listOf(
                MathQuizMaze.MazeItem(
                    formula = MathFormula.fromStringFullFormula("1+1=2"),
                    difficulty = QuestionDifficulty.Easy
                )
            )
        )

        mazeMathQuizRepository = MazeMathQuizRepositoryImpl(mathQuizCoreRepository, mazeQuizDaoMockk)
    }

    @Test
    fun `generate random maze`() = runTest {
        val random = Random(0)

        val questionSize = 100
        val operatorSize = 1
        val difficulty = QuestionDifficulty.Easy

        val maze = mazeMathQuizRepository.generateMaze(
            questionSize = questionSize,
            operatorSize = operatorSize,
            difficulty = difficulty,
            random = random
        )

        val mazeFormulas = maze.formulas

        assertThat(mazeFormulas).hasSize(questionSize)
        assertThat(mazeFormulas).containsNoDuplicates()

        val expressions = Expressions()

        mazeFormulas.forEach { item ->
            assertThat(item.difficulty).isEqualTo(difficulty)
            assertThat(item.formula.operatorSize).isEqualTo(operatorSize)

            val solution = expressions.eval(item.formula.leftFormula).toInt()
            assertThat(solution).isEqualTo(item.formula.solution)
        }
    }

    @Test
    fun `get saved maze quiz flow test`() = runTest {
        val res = mazeMathQuizRepository
            .getSavedMazeQuizFlow()
            .filter { it is Resource.Success || it is Resource.Error }
            .catch { thr -> fail(thr) }
            .single()

        assertThat(res).isInstanceOf(Resource.Success::class.java)

        val maze = res.data ?: fail("Math quiz maze is null")

        assertThat(maze.formulas).hasSize(1)
        assertThat(maze.formulas).containsExactly(
            MathQuizMaze.MazeItem(
                formula = MathFormula.fromStringFullFormula("1+1=2"),
                difficulty = QuestionDifficulty.Easy
            )
        )
    }
}