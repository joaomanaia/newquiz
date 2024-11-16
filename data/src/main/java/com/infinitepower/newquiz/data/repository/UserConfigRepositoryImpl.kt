package com.infinitepower.newquiz.data.repository

import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.domain.repository.UserConfigRepository
import com.infinitepower.newquiz.model.regional_preferences.DistanceUnitType
import com.infinitepower.newquiz.model.regional_preferences.RegionalPreferences
import com.infinitepower.newquiz.model.regional_preferences.TemperatureUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserConfigRepositoryImpl @Inject constructor(
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
) : UserConfigRepository {
    override suspend fun getRegionalPreferences(): RegionalPreferences {
        val temperatureUnitStr =
            settingsDataStoreManager.getPreference(SettingsCommon.TemperatureUnit)
        val temperatureUnit = if (temperatureUnitStr.isBlank()) {
            null // use default
        } else {
            TemperatureUnit.valueOf(temperatureUnitStr)
        }

        val distanceUnitTypeStr =
            settingsDataStoreManager.getPreference(SettingsCommon.DistanceUnitType)
        val distanceUnitType = if (distanceUnitTypeStr.isBlank()) {
            null // use default
        } else {
            DistanceUnitType.valueOf(distanceUnitTypeStr)
        }

        return RegionalPreferences(
            temperatureUnit = temperatureUnit,
            distanceUnitType = distanceUnitType
        )
    }
}
