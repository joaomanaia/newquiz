package com.infinitepower.newquiz.data.repository.maze_quiz

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.analytics.LocalDebugAnalyticsHelper
import com.infinitepower.newquiz.core.database.dao.MazeQuizDao
import com.infinitepower.newquiz.core.database.model.toEntity
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class MazeQuizRepositoryImplTest {
    private val mazeQuizDao = mockk<MazeQuizDao>()

    private lateinit var mazeQuizRepository: MazeQuizRepository

    @BeforeTest
    fun setup() {
        mazeQuizRepository = MazeQuizRepositoryImpl(
            mazeQuizDao = mazeQuizDao,
            analyticsHelper = LocalDebugAnalyticsHelper()
        )
    }

    @Test
    fun `get saved maze quiz flow test`() = runTest {
        val items = createItems()
        every {
            mazeQuizDao.getAllMazeItemsFlow()
        } returns flowOf(items.map(MazeQuiz.MazeItem::toEntity))

        mazeQuizRepository.getSavedMazeQuizFlow().test {
            assertThat(awaitItem().items).hasSize(10)
            awaitComplete()
        }
    }

    @Test
    fun `completeMazeItem should complete maze item`() = runTest {
        val items = createItems()
        val firstItem = items.first()

        coEvery { mazeQuizDao.getAllMazeItems() } returns items.map(MazeQuiz.MazeItem::toEntity)
        coJustRun { mazeQuizDao.updateItem(firstItem.toEntity().copy(played = true)) }

        mazeQuizRepository.completeMazeItem(firstItem.id)

        coVerify(exactly = 1) {
            mazeQuizDao.getAllMazeItems()
            mazeQuizDao.updateItem(firstItem.toEntity().copy(played = true))
        }
        confirmVerified()
    }

    private fun createItems(count: Int = 10): List<MazeQuiz.MazeItem> {
        return List(count) {
            MazeQuiz.MazeItem.Wordle(
                difficulty = QuestionDifficulty.Easy,
                wordleWord = WordleWord("1+1=2"),
                wordleQuizType = WordleQuizType.MATH_FORMULA,
                mazeSeed = 0
            )
        }
    }
}