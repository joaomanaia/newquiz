package com.infinitepower.newquiz.quiz_presentation

import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.infinitepower.newquiz.model.question.Question
import com.infinitepower.newquiz.model.question.QuestionStep

@Keep
data class QuizScreenUiState(
    val questionSteps: List<QuestionStep> = emptyList(),
    val currentQuestionIndex: Int = -1,
    val selectedAnswer: SelectedAnswer = SelectedAnswer.NONE,
    val remainingTime: RemainingTime = RemainingTime.ZERO,
) {
    val currentQuestionStep: QuestionStep.Current? = questionSteps.getOrNull(currentQuestionIndex)?.asCurrent()

    @Composable
    @ReadOnlyComposable
    fun getQuestionPositionFormatted(): String =
        "Question ${currentQuestionIndex + 1}/${questionSteps.size}"

    fun getNextIndex(): Int = if (currentQuestionIndex == questionSteps.lastIndex) -1 else currentQuestionIndex + 1
}

@JvmInline
value class SelectedAnswer private constructor(val index: Int) {
    companion object {
        val NONE = SelectedAnswer(-1)

        fun fromIndex(index: Int): SelectedAnswer = SelectedAnswer(index)
    }

    private val isNone: Boolean
        get() = index == -1

    val isSelected: Boolean
        get() = !isNone

    fun isCorrect(question: Question): Boolean =
        !isNone && question.correctAns == index

    init {
        require(index >= -1) { "SelectedAnswer index must be greater than -1" }
    }
}

@JvmInline
value class RemainingTime private constructor(val value: Long) {
    companion object {
        val ZERO = RemainingTime(0L)

        fun fromValue(value: Long): RemainingTime = RemainingTime(value)
    }

    init {
        require(value >= 0L) { "RemainingTime value must be greater than 0" }
    }

    fun getRemainingPercent(): Float = value.toFloat() / QUIZ_COUNTDOWN_IN_MILLIS

    fun toMinuteSecond(): String {
        val minutes = (value / 1000) / 60
        val seconds = (value / 1000) % 60
        return if (minutes == 0L) seconds.toString() else "$minutes:$seconds"
    }
}