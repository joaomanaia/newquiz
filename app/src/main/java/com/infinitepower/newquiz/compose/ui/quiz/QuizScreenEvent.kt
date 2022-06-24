package com.infinitepower.newquiz.compose.ui.quiz

sealed class QuizScreenEvent {
    object OnVerifyQuestionClick : QuizScreenEvent()

    data class OnOptionClick(
        val position: Int
    ) : QuizScreenEvent()

    object OnSaveButtonClick : QuizScreenEvent()

    data class UpdateDataAndStartQuiz(
        val quizOption: com.infinitepower.newquiz.compose.quiz_presentation.QuizType,
        val defaultQuestionsString: String
    ) : QuizScreenEvent()
}