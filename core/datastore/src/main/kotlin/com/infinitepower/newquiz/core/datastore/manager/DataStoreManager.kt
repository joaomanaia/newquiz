package com.infinitepower.newquiz.core.datastore.manager

import androidx.datastore.preferences.core.Preferences
import com.infinitepower.newquiz.core.datastore.PreferenceRequest
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    val preferenceFlow: Flow<Preferences>

    suspend fun <T> getPreference(preferenceEntry: PreferenceRequest<T>): T

    fun <T> getPreferenceFlow(request: PreferenceRequest<T>): Flow<T>

    suspend fun <T> editPreference(key: Preferences.Key<T>, newValue: T)

    suspend fun editPreferences(vararg prefs: Preferences.Pair<*>)

    suspend fun clearPreferences()
}
