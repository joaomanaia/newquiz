package com.infinitepower.newquiz.compose.quiz_presentation

import androidx.annotation.Keep
import com.infinitepower.newquiz.compose.model.question.Question
import com.infinitepower.newquiz.compose.model.question.QuestionStep

@Keep
data class QuizScreenUiState(
    val questions: List<Question> = emptyList(),
    val questionSteps: List<QuestionStep> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val currentQuestion: Question? = null,
    val selectedAnswer: SelectedAnswer = SelectedAnswer.NONE
) {
    val questionPositionFormatted: String
        get() = "${currentQuestionIndex + 1}/${questions.size}"
}

@JvmInline
value class SelectedAnswer private constructor(val index: Int) {
    companion object {
        val NONE = SelectedAnswer(-1)

        fun fromIndex(index: Int): SelectedAnswer = SelectedAnswer(index)
    }

    val isNone: Boolean
        get() = index == -1

    init {
        require(index > -1) { "SelectedAnswer index must be greater than -1" }
    }
}