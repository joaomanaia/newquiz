package com.infinitepower.newquiz.data.repository.multi_choice_quiz.dto

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionType
import com.infinitepower.newquiz.model.multi_choice_quiz.QuestionLanguage
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.util.base64.base64Decoded
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class OpenTDBQuestionResponse(
    val response_code: Int,
    val results: List<OpenTDBResult>
) : java.io.Serializable {
    @Keep
    @Serializable
    data class OpenTDBResult(
        val category: String,
        val type: String,
        val difficulty: String,
        val question: String,
        val correct_answer: String,
        val incorrect_answers: List<String>
    ) : java.io.Serializable {
        private fun decodeBase64OpenTDBResult(): OpenTDBResult = copy(
            category = category.base64Decoded,
            type = type.base64Decoded,
            difficulty = difficulty.base64Decoded,
            question = question.base64Decoded,
            correct_answer = correct_answer.base64Decoded,
            incorrect_answers = incorrect_answers.map { answer -> answer.base64Decoded }
        )

        private fun toQuestion(): MultiChoiceQuestion {
            val answers = incorrect_answers.plus(correct_answer).shuffled()
            val correctAnswerIndex = answers.indexOf(correct_answer)

            return MultiChoiceQuestion(
                description = question,
                answers = answers,
                category = MultiChoiceBaseCategory.fromId(category),
                correctAns = correctAnswerIndex,
                type = MultiChoiceQuestionType.valueOf(type.uppercase()),
                difficulty = QuestionDifficulty.from(difficulty),
                lang = QuestionLanguage.EN
            )
        }

        fun decodeResultToQuestion(): MultiChoiceQuestion = decodeBase64OpenTDBResult().toQuestion()
    }
}