package com.infinitepower.newquiz.model.country

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.question.QuestionDifficulty

@Keep
data class Country(
    val countryCode: String,
    val countryName: String,
    val capital: String,
    val continent: Continent,
    val difficulty: QuestionDifficulty,
    val flagUrl: String
)
