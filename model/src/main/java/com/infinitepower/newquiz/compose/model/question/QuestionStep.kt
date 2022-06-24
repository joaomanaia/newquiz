package com.infinitepower.newquiz.compose.model.question

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
    ) : QuestionStep()

    @Keep
    @Serializable
    data class Current(
        override val question: Question
    ) : QuestionStep()

    @Keep
    @Serializable
    data class Completed(
        override val question: Question,
        val correct: Boolean
    ) : QuestionStep()
}