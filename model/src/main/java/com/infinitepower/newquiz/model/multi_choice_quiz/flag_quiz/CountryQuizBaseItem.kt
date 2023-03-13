package com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CountryQuizBaseItem(
    val countryCode: String,
    val countryName: String,
    val capital: String,
    val continent: String,
    val difficulty: String
) : java.io.Serializable

