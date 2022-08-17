package com.infinitepower.newquiz.quiz_presentation

import com.infinitepower.newquiz.model.question.SelectedAnswer

sealed class QuizScreenUiEvent {
    data class SelectAnswer(val answer: SelectedAnswer) : QuizScreenUiEvent()

    object VerifyAnswer : QuizScreenUiEvent()

    object SaveQuestion : QuizScreenUiEvent()
}