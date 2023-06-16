package com.infinitepower.newquiz.data.repository.daily_challenge.util

import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.util.translation.getWordleTitle
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.core.R as CoreR

fun GameEvent.getTitle(
    maxValue: Int,
    comparisonQuizCategories: List<ComparisonQuizCategory>
): UiText {
    return when (this) {
        // Multi choice quiz
        GameEvent.MultiChoice.PlayRandomQuiz -> UiText.PluralStringResource(
            resId = CoreR.plurals.play_random_multi_choice_quiz_game,
            quantity = maxValue,
            maxValue
        )
        GameEvent.MultiChoice.EndQuiz -> UiText.PluralStringResource(
            resId = CoreR.plurals.end_multi_choice_quiz_game,
            quantity = maxValue,
            maxValue
        )
        GameEvent.MultiChoice.PlayQuestions -> UiText.PluralStringResource(
            resId = CoreR.plurals.play_multi_choice_quiz_questions,
            quantity = maxValue,
            maxValue
        )
        GameEvent.MultiChoice.GetAnswersCorrect -> UiText.PluralStringResource(
            resId = CoreR.plurals.get_multi_choice_quiz_answer_correct,
            quantity = maxValue,
            maxValue
        )
        is GameEvent.MultiChoice.PlayQuizWithCategory -> {
            val category = multiChoiceQuestionCategories.first { it.id == this.categoryId }

            UiText.PluralStringResource(
                resId = CoreR.plurals.play_multi_choice_quiz_game_in_category,
                quantity = maxValue,
                maxValue,
                category.name
            )
        }

        // Wordle
        GameEvent.Wordle.GetWordCorrect -> UiText.PluralStringResource(
            resId = CoreR.plurals.get_wordle_word_correct,
            quantity = maxValue,
            maxValue
        )
        is GameEvent.Wordle.PlayWordWithCategory -> {
            val categoryName = wordleCategory.getWordleTitle()

            UiText.PluralStringResource(
                resId = CoreR.plurals.play_wordle_game_in_category,
                quantity = maxValue,
                maxValue,
                categoryName
            )
        }

        // Comparison quiz
        is GameEvent.ComparisonQuiz.PlayQuizWithCategory -> {
            val categoryName = comparisonQuizCategories
                .find { it.id == this.categoryId }
                ?.name ?: ""

            UiText.PluralStringResource(
                resId = CoreR.plurals.play_comparison_quiz_game_in_category,
                quantity = maxValue,
                maxValue,
                categoryName
            )
        }
        is GameEvent.ComparisonQuiz.PlayAndGetScore -> UiText.PluralStringResource(
            resId = CoreR.plurals.play_comparison_quiz_game_and_get_score,
            quantity = maxValue,
            maxValue,
            score
        )
        is GameEvent.ComparisonQuiz.PlayWithComparisonMode -> {
            val modeName = if (mode == ComparisonMode.GREATER) {
                UiText.StringResource(CoreR.string.greater)
            } else {
                UiText.StringResource(CoreR.string.lesser)
            }

            UiText.PluralStringResource(
                resId = CoreR.plurals.play_comparison_quiz_game_in_comparison_mode,
                quantity = maxValue,
                maxValue,
                modeName
            )
        }
    }
}
