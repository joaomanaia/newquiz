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
        fun changeToCompleted(correct: Boolean) = Completed(question, correct)
    }

    @Keep
    @Serializable
    data class Completed(
        override val question: Question,
        val correct: Boolean
    ) : QuestionStep()

    fun asCurrent() = Current(question)
}