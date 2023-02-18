package com.infinitepower.newquiz.data.local.math_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.data.repository.maze_quiz.MazeQuizRepositoryImpl
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizDao
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.maze.MazeQuizItemEntity
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
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

@OptIn(ExperimentalCoroutinesApi::class)
internal class MazeMathQuizRepositoryImplTest {
    private val mazeQuizDaoMockk = mockk<MazeQuizDao>()

    private lateinit var mazeQuizRepository: MazeQuizRepository

    @BeforeEach
    fun setup() {
        every {
            mazeQuizDaoMockk.getAllMazeItemsFlow()
        } returns flowOf(
            listOf(
                MazeQuizItemEntity(
                    difficulty = QuestionDifficulty.Easy,
                    type = MazeQuizItemEntity.Type.WORDLE,
                    wordleItem = MazeQuizItemEntity.WordleEntity(
                        wordleWord = "1+1=2",
                        wordleQuizType = WordleQuizType.MATH_FORMULA
                    ),
                    played = false,
                    mazeSeed = 0
                )
            )
        )

        mazeQuizRepository = MazeQuizRepositoryImpl(mazeQuizDaoMockk)
    }

    @Test
    fun `get saved maze quiz flow test`() = runTest {
        val res = mazeQuizRepository
            .getSavedMazeQuizFlow()
            .filter { it is Resource.Success || it is Resource.Error }
            .catch { thr -> fail(thr) }
            .single()

        assertThat(res).isInstanceOf(Resource.Success::class.java)

        val maze = res.data ?: fail("Math quiz maze is null")

        assertThat(maze.items).hasSize(1)
        assertThat(maze.items).containsExactly(
            MazeQuiz.MazeItem.Wordle(
                difficulty = QuestionDifficulty.Easy,
                wordleWord = WordleWord("1+1=2"),
                wordleQuizType = WordleQuizType.MATH_FORMULA,
                mazeSeed = 0
            )
        )
    }
}