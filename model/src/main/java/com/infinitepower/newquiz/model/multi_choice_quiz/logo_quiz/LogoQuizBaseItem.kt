package com.infinitepower.newquiz.model.multi_choice_quiz.logo_quiz

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LogoQuizBaseItem(
    val description: String,
    val name: String,
    @SerialName("img_url") val imgUrl: String,
    @SerialName("incorrect_answers") val incorrectAnswers: List<String>,
    val difficulty: String
)
