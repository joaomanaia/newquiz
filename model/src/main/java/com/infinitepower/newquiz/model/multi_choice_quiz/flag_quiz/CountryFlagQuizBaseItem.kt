package com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.common.BaseUrls
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CountryFlagQuizBaseItem(
    val code: String,
    val name: String,
    val flagUrl: String = "${BaseUrls.FLAG_BASE_URL}/$code"
)