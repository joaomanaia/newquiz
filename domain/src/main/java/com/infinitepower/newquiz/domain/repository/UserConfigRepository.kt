package com.infinitepower.newquiz.domain.repository

import com.infinitepower.newquiz.model.regional_preferences.RegionalPreferences

interface UserConfigRepository {
    suspend fun getRegionalPreferences(): RegionalPreferences
}
