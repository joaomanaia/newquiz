package com.infinitepower.newquiz.data.repository.country

import android.content.Context
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.util.android.resources.readRawJson
import com.infinitepower.newquiz.data.R
import com.infinitepower.newquiz.domain.repository.CountryRepository
import com.infinitepower.newquiz.model.country.Continent
import com.infinitepower.newquiz.model.country.Country
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteConfig: RemoteConfig
) : CountryRepository {
    companion object {
        /**
         * The string that is used to indicate that the flag should be loaded from the local
         * resources.
         */
        private const val LOCAL_FLAG_BASE_URL = "local"
    }

    private fun getCountryFlag(
        countryCode: String,
        flagBaseUrl: String
    ): URI {
        // Check if the flag loads from the local resource or from the remote server
        if (flagBaseUrl == LOCAL_FLAG_BASE_URL) {
            // Get the flag uri from the assets folder
            return URI.create("file:///android_asset/flags/${countryCode.lowercase()}.svg")
        }

        // Load the flag from the url
        val flagUrl = flagBaseUrl.replace("%code%", countryCode.lowercase())

        return URI.create(flagUrl)
    }

    override suspend fun getAllCountries(): List<Country> {
        val flagBaseUrl = remoteConfig.get(RemoteConfigValue.FLAG_BASE_URL)

        return getCountryFromJson().map { entity ->
            val flagImage = getCountryFlag(entity.countryCode, flagBaseUrl)

            Country(
                countryCode = entity.countryCode,
                countryName = entity.countryName,
                capital = entity.capital,
                population = entity.population,
                area = entity.area,
                continent = Continent.from(entity.continent),
                difficulty = QuestionDifficulty.from(entity.difficulty),
                flagImage = flagImage
            )
        }
    }

    private suspend fun getCountryFromJson(): List<CountryEntity> {
        return context
            .resources
            .readRawJson<List<CountryEntity>>(R.raw.all_countries)
    }
}
