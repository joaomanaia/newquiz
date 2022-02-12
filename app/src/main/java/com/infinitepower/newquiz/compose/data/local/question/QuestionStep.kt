package com.infinitepower.newquiz.compose.data.local.question

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class QuestionStep : java.io.Serializable {
    companion object {
        val QuestionStep.questionId: Int
            get() = when(this) {
                is NotCurrent -> question.id
                is Current -> question.id
                is Completed -> question.id
            }
    }

    @Keep
    @Serializable
    data class NotCurrent(
        val question: Question
    ) : QuestionStep()

    @Keep
    @Serializable
    data class Current(
        val question: Question
    ) : QuestionStep()

    @Keep
    @Serializable
    data class Completed(
        val question: Question,
        val correct: Boolean
    ) : QuestionStep()
}