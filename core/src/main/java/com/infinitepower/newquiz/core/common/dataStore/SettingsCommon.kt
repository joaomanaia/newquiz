package com.infinitepower.newquiz.core.common.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.infinitepower.newquiz.core.dataStore.manager.PreferenceRequest

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SettingsCommon {
    object ShowLoginCard : PreferenceRequest<Boolean>(booleanPreferencesKey("showLoginCard"), true)
}