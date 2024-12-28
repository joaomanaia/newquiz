package com.infinitepower.newquiz.feature.maze

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.testing.data.fake.FakeComparisonQuizData
import com.infinitepower.newquiz.model.GameMode
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import kotlinx.collections.immutable.toImmutableList
import kotlin.test.Test

internal class MazeScreenUiStateTest {
    @Test
    fun `getAvailableCategoriesByGameMode returns empty when maze is empty`() {
        val maze = MazeQuiz(items = generateMaze().toImmutableList())

        val uiState = MazeScreenUiState(
            loading = false,
            maze = maze
        )

        val availableCategoriesByGameMode = uiState.getAvailableCategoriesByGameMode()
        assertThat(availableCategoriesByGameMode).isEmpty()
    }

    @Test
    fun `getAvailableCategoriesByGameMode returns all categories when all categories are available`() {
        val maze = MazeQuiz(
            items = generateMaze(
                multiChoiceQuestionCount = 1,
                wordleQuestionCount = 1,
                comparisonQuizQuestionCount = 1
            ).toImmutableList(),
        )

        val comparisonQuizCategory = FakeComparisonQuizData.generateCategory(1)

        val uiState = MazeScreenUiState(
            loading = false,
            maze = maze,
            comparisonQuizCategories = listOf(comparisonQuizCategory)
        )

        val availableCategoriesByGameMode = uiState.getAvailableCategoriesByGameMode()
        availableCategoriesByGameMode.forEach { (gameMode, baseCategories) ->
            println("Game Mode: $gameMode")
            assertThat(baseCategories).hasSize(1)
        }
    }

    @Test
    fun `getAvailableCategoriesByGameMode returns only available categories`() {
        val maze = MazeQuiz(
            items = generateMaze(
                multiChoiceQuestionCount = 1,
                wordleQuestionCount = 1,
                comparisonQuizQuestionCount = 1
            ).toImmutableList(),
        )

        val uiState = MazeScreenUiState(
            loading = false,
            maze = maze,
            comparisonQuizCategories = emptyList() // No available comparison quiz categories
        )

        val availableCategoriesByGameMode = uiState.getAvailableCategoriesByGameMode()
        availableCategoriesByGameMode.forEach { (gameMode, baseCategories) ->
            if (gameMode == GameMode.COMPARISON_QUIZ) {
                assertThat(baseCategories).hasSize(0)
            } else {
                assertThat(baseCategories).hasSize(1)
            }
        }
    }

    @Test
    fun `getInvalidMazeItems returns correct items`() {
        val maze = MazeQuiz(
            items = generateMaze(
                multiChoiceQuestionCount = 3,
                wordleQuestionCount = 5,
                comparisonQuizQuestionCount = 5 // 5 Invalid questions
            ).toImmutableList(),
        )

        val uiState = MazeScreenUiState(
            loading = false,
            maze = maze,
            comparisonQuizCategories = emptyList() // No available comparison quiz categories
        )

        val availableCategoriesByGameMode = uiState.getAvailableCategoriesByGameMode()
        val invalidItems = uiState.getInvalidMazeItems(availableCategoriesByGameMode)
        assertThat(invalidItems).hasSize(5)
        invalidItems.forEach { item ->
            assertThat(item.gameMode).isEqualTo(GameMode.COMPARISON_QUIZ)
        }
    }

    private fun generateMaze(
        seed: Int = 0,
        multiChoiceQuestionCount: Int = 0,
        wordleQuestionCount: Int = 0,
        comparisonQuizQuestionCount: Int = 0
    ): List<MazeQuiz.MazeItem> {
        val items = mutableListOf<MazeQuiz.MazeItem>()

        List(multiChoiceQuestionCount) {
            MazeQuiz.MazeItem.MultiChoice(
                question = MultiChoiceQuestion(
                    id = it,
                    description = "description",
                    answers = listOf("a", "b", "c"),
                    lang = QuestionLanguage.EN,
                    category = MultiChoiceBaseCategory.Flag,
                    correctAns = 0,
                    type = MultiChoiceQuestionType.MULTIPLE,
                    difficulty = QuestionDifficulty.Easy
                ),
                mazeSeed = seed,
            )
        }.also { items.addAll(it) }

        List(wordleQuestionCount) {
            MazeQuiz.MazeItem.Wordle(
                wordleWord = WordleWord(""),
                wordleQuizType = WordleQuizType.TEXT,
                mazeSeed = seed,
            )
        }.also { items.addAll(it) }

        List(comparisonQuizQuestionCount) {
            MazeQuiz.MazeItem.ComparisonQuiz(
                question = FakeComparisonQuizData.generateQuestion(categoryId = "1"),
                mazeSeed = seed,
            )
        }.also { items.addAll(it) }

        return items
    }
}
