package com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CountryFlagQuizBaseItem(
    val code: String,
    val name: String
)