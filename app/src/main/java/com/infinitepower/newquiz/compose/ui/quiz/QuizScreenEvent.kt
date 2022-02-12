package com.infinitepower.newquiz.compose.ui.quiz

import com.infinitepower.newquiz.compose.data.local.question.Question

sealed class QuizScreenEvent {
    object OnVerifyQuestionClick : QuizScreenEvent()

    data class OnOptionClick(
        val position: Int
    ) : QuizScreenEvent()

    object OnSaveButtonClick : QuizScreenEvent()

    data class UpdateDataAndStartQuiz(
        val quizOption: QuizOption,
        val defaultQuestionsString: String
    ) : QuizScreenEvent()
}