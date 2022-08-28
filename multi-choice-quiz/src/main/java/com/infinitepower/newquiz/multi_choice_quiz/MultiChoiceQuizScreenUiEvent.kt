package com.infinitepower.newquiz.multi_choice_quiz

import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer

sealed class MultiChoiceQuizScreenUiEvent {
    data class SelectAnswer(val answer: SelectedAnswer) : MultiChoiceQuizScreenUiEvent()

    object VerifyAnswer : MultiChoiceQuizScreenUiEvent()

    object SaveQuestion : MultiChoiceQuizScreenUiEvent()
}