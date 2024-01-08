package com.infinitepower.newquiz.feature.daily_challenge

import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.wordle.WordleQuizType

interface DailyChallengeScreenNavigator {
    fun navigateWithGameEvent(
        event: GameEvent,
        comparisonQuizCategories: List<ComparisonQuizCategory>
    ) {
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
                val categoryKey = event.categoryId
                val category = MultiChoiceBaseCategory.fromId(categoryKey)

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
                val randomCategory = comparisonQuizCategories.random()
                navigateToComparisonQuiz(randomCategory)
            }
            is GameEvent.ComparisonQuiz.PlayQuizWithCategory -> {
                val categoryId = event.categoryId
                val category = comparisonQuizCategories.find { it.id == categoryId } ?: return

                navigateToComparisonQuiz(category)
            }
            is GameEvent.ComparisonQuiz.PlayWithComparisonMode -> {
                val randomCategory = comparisonQuizCategories.random()

                navigateToComparisonQuiz(randomCategory, event.mode)
            }
        }
    }

    fun navigateToMultiChoiceQuiz(
        category: MultiChoiceBaseCategory = MultiChoiceBaseCategory.Random
    )

    fun navigateToWordleQuiz(
        type: WordleQuizType = WordleQuizType.entries.random()
    )

    fun navigateToComparisonQuiz(
        category: ComparisonQuizCategory,
        mode: ComparisonMode = ComparisonMode.entries.random()
    )
}
