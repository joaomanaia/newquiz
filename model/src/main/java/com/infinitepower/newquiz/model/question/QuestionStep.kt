package com.infinitepower.newquiz.model.question

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class QuestionStep {
    abstract val question: Question

    @Keep
    @Serializable
    data class NotCurrent(
        override val question: Question
    ) : QuestionStep() {
        fun changeToCurrent() = Current(question)
    }

    @Keep
    @Serializable
    data class Current(
        override val question: Question
    ) : QuestionStep() {
        fun changeToCompleted(
            correct: Boolean,
            selectedAnswer: SelectedAnswer
        ) = Completed(question, correct, selectedAnswer)
    }

    @Keep
    @Serializable
    data class Completed(
        override val question: Question,
        val correct: Boolean,
        val selectedAnswer: SelectedAnswer = SelectedAnswer.NONE
    ) : QuestionStep()

    fun asCurrent() = Current(question)
}

fun List<QuestionStep>.isAllCompleted(): Boolean = all { it is QuestionStep.Completed }

fun List<QuestionStep.Completed>.countCorrectQuestions(): Int = count { it.correct }