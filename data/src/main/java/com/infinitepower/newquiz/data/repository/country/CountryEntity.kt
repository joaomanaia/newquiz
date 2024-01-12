package com.infinitepower.newquiz.data.repository.country

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.country.Continent
import com.infinitepower.newquiz.model.country.Country
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.serialization.Serializable

@Keep
@Serializable
internal data class CountryEntity(
    val countryCode: String,
    val countryName: String,
    val capital: String,
    val continent: String,
    val difficulty: String,
    val population: Long,
    val area: Double,
) : java.io.Serializable

internal fun CountryEntity.toModel(flagBaseUrl: String): Country {
    // Format the url like https://flagapi.example/svg/%code%.svg
    val flagUrl = flagBaseUrl.replace("%code%", countryCode.lowercase())

    return Country(
        countryCode = countryCode,
        countryName = countryName,
        capital = capital,
        population = population,
        area = area,
        continent = Continent.from(continent),
        difficulty = QuestionDifficulty.from(difficulty),
        flagUrl = flagUrl
    )
}
