package com.infinitepower.newquiz.model.country

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.question.QuestionDifficulty

/**
 * Data class representing a country.
 *
 * @property countryCode The alpha-2 country code.
 * @property countryName The country name.
 * @property capital The capital of the country.
 * @property population The population of the country.
 * @property area The area of the country, in square kilometers.
 * @property continent The continent the country is in.
 * @property difficulty The difficulty of the question.
 * @property flagUrl The URL of the flag of the country.
 */
@Keep
data class Country(
    val countryCode: String,
    val countryName: String,
    val capital: String,
    val population: Long,
    val area: Double,
    val continent: Continent,
    val difficulty: QuestionDifficulty,
    val flagUrl: String
)
