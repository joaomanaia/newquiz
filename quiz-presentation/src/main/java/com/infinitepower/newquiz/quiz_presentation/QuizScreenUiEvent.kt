package com.infinitepower.newquiz.quiz_presentation

sealed class QuizScreenUiEvent {
    data class SelectAnswer(val answer: SelectedAnswer) : QuizScreenUiEvent()

    object VerifyAnswer : QuizScreenUiEvent()

    object SaveQuestion : QuizScreenUiEvent()
}