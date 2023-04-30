package com.infinitepower.newquiz.data.repository.daily_challenge.util

import android.content.Context
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.core.R as CoreR

fun GameEvent.getTitle(
    maxValue: Int,
    context: Context
): UiText {
    return when (this) {
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
            val category = multiChoiceQuestionCategories.first { it.key == this.categoryKey }
            val categoryName = category.name.asString(context)

            UiText.PluralStringResource(
                resId = CoreR.plurals.play_multi_choice_quiz_game_in_category,
                quantity = maxValue,
                maxValue,
                categoryName
            )
        }
    }
}
