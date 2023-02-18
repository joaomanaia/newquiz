package com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

private const val FLAG_BASE_URL = "https://countryflagsapi.com/png"

@Keep
@Serializable
data class CountryFlagQuizBaseItem(
    val code: String,
    val name: String,
    val flagUrl: String = "$FLAG_BASE_URL/$code"
)