package com.infinitepower.newquiz.model.question.opentdb

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.question.Question
import com.infinitepower.newquiz.model.util.base64.base64Decoded
import kotlinx.serialization.Serializable
import java.security.SecureRandom

@Keep
@Serializable
data class OpenTDBQuestionResponse(
    val response_code: Int,
    val results: List<OpenTDBResult>
) {
    @Keep
    @Serializable
    data class OpenTDBResult(
        val category: String,
        val type: String,
        val difficulty: String,
        val question: String,
        val correct_answer: String,
        val incorrect_answers: List<String>
    ) {
        private fun decodeBase64OpenTDBResult(): OpenTDBResult = copy(
            category = category.base64Decoded,
            type = type.base64Decoded,
            difficulty = difficulty.base64Decoded,
            question = question.base64Decoded,
            correct_answer = correct_answer.base64Decoded,
            incorrect_answers = incorrect_answers.map { answer -> answer.base64Decoded }
        )

        private fun toQuestion(
            id: Int
        ): Question {
            val answers = incorrect_answers.plus(correct_answer).shuffled()
            val correctAnswerIndex = answers.indexOf(correct_answer)

            return Question(
                id = SecureRandom().nextInt(),
                description = question,
                answers = answers,
                category = category,
                correctAns = correctAnswerIndex,
                type = type,
                difficulty = difficulty,
                lang = "EN"
            )
        }

        fun decodeResultToQuestion(id: Int): Question = decodeBase64OpenTDBResult().toQuestion(id)
    }
}