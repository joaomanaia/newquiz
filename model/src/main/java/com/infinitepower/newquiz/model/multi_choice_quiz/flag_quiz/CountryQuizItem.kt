package com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.common.BaseUrls
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CountryQuizItem(
    val countryCode: String,
    val countryName: String,
    val capital: String,
    val continent: Continent,
    val difficulty: QuestionDifficulty,
    val flagUrl: String = "${BaseUrls.FLAG_BASE_URL}/$countryCode"
) : java.io.Serializable
