package com.infinitepower.newquiz.data.repository.country

import android.content.Context
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.util.android.resources.readRawJson
import com.infinitepower.newquiz.data.R
import com.infinitepower.newquiz.domain.repository.CountryRepository
import com.infinitepower.newquiz.model.country.Country
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteConfig: RemoteConfig
) : CountryRepository {
    override suspend fun getAllCountries(): List<Country> {
        val flagBaseUrl = remoteConfig.get(RemoteConfigValue.FLAG_BASE_URL)

        return getCountryFromJson().map { it.toModel(flagBaseUrl = flagBaseUrl) }
    }

    private suspend fun getCountryFromJson(): List<CountryEntity> {
        return context
            .resources
            .readRawJson<List<CountryEntity>>(R.raw.all_countries)
    }
}
