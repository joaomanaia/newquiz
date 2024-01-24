package com.infinitepower.newquiz.model.number

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

/**
 * Entity for NumberTriviaQuestions
 *
 * @property message error message
 */
@Keep
@Serializable
data class NumberTriviaQuestionsEntity(
    val questions: List<NumberTriviaQuestionEntity>,
    val message: String? = null
) : java.io.Serializable {
    fun toNumberTriviaQuestions(): List<NumberTriviaQuestion> {
        if (message != null) error(message)

        return questions.map { entity ->
            NumberTriviaQuestion(
                number = entity.number,
                question = entity.question
            )
        }
    }
}

@Keep
@Serializable
data class NumberTriviaQuestionEntity(
    val number: Int,
    val question: String
) : java.io.Serializable
