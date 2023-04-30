package com.infinitepower.newquiz.daily_challenge

import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory

interface DailyChallengeScreenNavigator {
    fun navigateWithGameEvent(event: GameEvent) {
        when (event) {
            // Navigate to random multi choice quiz
            is GameEvent.MultiChoice.PlayRandomQuiz,
            GameEvent.MultiChoice.EndQuiz,
            GameEvent.MultiChoice.PlayQuestions,
            GameEvent.MultiChoice.GetAnswersCorrect -> {
                navigateToMultiChoiceQuiz()
            }

            // Navigate to multi choice quiz with category
            is GameEvent.MultiChoice.PlayQuizWithCategory -> {
                val categoryKey = event.categoryKey
                val category = MultiChoiceBaseCategory.fromKey(categoryKey)

                navigateToMultiChoiceQuiz(category)
            }
        }
    }

    fun navigateToMultiChoiceQuiz(
        category: MultiChoiceBaseCategory = MultiChoiceBaseCategory.Random
    )
}