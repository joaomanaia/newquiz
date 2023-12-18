package com.infinitepower.newquiz.data.repository.country

import com.infinitepower.newquiz.domain.repository.CountryRepository
import com.infinitepower.newquiz.model.country.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

/**
 * This class is used for loading countries from local json file without needing
 * to use android context. Only used for testing.
 */
internal class TestCountryRepositoryImpl : CountryRepository {
    override suspend fun getAllCountries(): List<Country> {
        val flagBaseUrl = "https://example.com/%code%"

        return getCountryFromJson().map { it.toModel(flagBaseUrl) }
    }

    private suspend fun getCountryFromJson(): List<CountryEntity> {
        return withContext(Dispatchers.IO) {
            val path = "src/main/res/raw"
            val file = File(path, "all_countries.json")

            val strRes = file.readText()

            Json.decodeFromString(strRes)
        }
    }
}
