package com.infinitepower.newquiz.model.multi_choice_quiz.flag_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.common.BaseUrls
import com.infinitepower.newquiz.model.question.QuestionDifficulty
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

fun CountryQuizBaseItem.toCountryQuizItem() = CountryQuizItem(
    countryCode = countryCode,
    countryName = countryName,
    capital = capital,
    continent = Continent.fromName(continent),
    difficulty = QuestionDifficulty.from(difficulty)
)
