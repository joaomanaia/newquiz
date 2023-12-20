package com.infinitepower.newquiz.core.datastore.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.infinitepower.newquiz.core.datastore.PreferenceRequest
import com.infinitepower.newquiz.model.DataAnalyticsConsentState

val Context.dataAnalyticsDataStore: DataStore<Preferences> by preferencesDataStore(name = "data_analytics")

object DataAnalyticsCommon {
    object DataAnalyticsConsent : PreferenceRequest<String>(stringPreferencesKey("dataAnalyticsConsent"), DataAnalyticsConsentState.NONE.name)

    object GloballyAnalyticsCollectionEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("dataAnalyticsEnabled"), false)

    object GeneralAnalyticsEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("generalAnalyticsEnabled"), false)

    object CrashlyticsEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("crashlyticsEnabled"), false)

    object PerformanceMonitoringEnabled : PreferenceRequest<Boolean>(booleanPreferencesKey("performanceMonitoringEnabled"), false)
}