package com.infinitepower.newquiz.daily_challenge

import com.infinitepower.newquiz.data.repository.comparison_quiz.ComparisonQuizCategoriesData
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.wordle.WordleQuizType

interface DailyChallengeScreenNavigator {
    fun navigateWithGameEvent(event: GameEvent) {
        when (event) {
            // Navigate to random multi choice quiz
            is GameEvent.MultiChoice.PlayRandomQuiz,
            is GameEvent.MultiChoice.EndQuiz,
            is GameEvent.MultiChoice.PlayQuestions,
            is GameEvent.MultiChoice.GetAnswersCorrect -> {
                navigateToMultiChoiceQuiz()
            }

            // Navigate to multi choice quiz with category
            is GameEvent.MultiChoice.PlayQuizWithCategory -> {
                val categoryKey = event.categoryKey
                val category = MultiChoiceBaseCategory.fromKey(categoryKey)

                navigateToMultiChoiceQuiz(category)
            }

            // Navigate to wordle quiz
            is GameEvent.Wordle.GetWordCorrect -> {
                navigateToWordleQuiz()
            }
            is GameEvent.Wordle.PlayWordWithCategory -> {
                navigateToWordleQuiz(event.wordleCategory)
            }

            // Navigate to comparison quiz
            is GameEvent.ComparisonQuiz.PlayAndGetScore -> {
                val randomCategory = ComparisonQuizCategoriesData.getCategories().random()
                navigateToComparisonQuiz(randomCategory)
            }
            is GameEvent.ComparisonQuiz.PlayQuizWithCategory -> {
                val categoryId = event.categoryId
                val category = ComparisonQuizCategoriesData
                    .getCategories()
                    .find { it.id == categoryId } ?: return

                navigateToComparisonQuiz(category)
            }
            is GameEvent.ComparisonQuiz.PlayWithComparisonMode -> {
                val randomCategory = ComparisonQuizCategoriesData.getCategories().random()

                navigateToComparisonQuiz(randomCategory, event.mode)
            }
        }
    }

    fun navigateToMultiChoiceQuiz(
        category: MultiChoiceBaseCategory = MultiChoiceBaseCategory.Random
    )

    fun navigateToWordleQuiz(
        type: WordleQuizType = WordleQuizType.values().random()
    )

    fun navigateToComparisonQuiz(
        category: ComparisonQuizCategory,
        mode: ComparisonModeByFirst = ComparisonModeByFirst.values().random()
    )
}