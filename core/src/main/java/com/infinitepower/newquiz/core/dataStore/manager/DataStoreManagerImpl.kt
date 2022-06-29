package com.infinitepower.newquiz.core.dataStore.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>
): DataStoreManager {
    override val preferenceFlow = dataStore.data

    override suspend fun <T> getPreference(preferenceEntry: PreferenceRequest<T>) =
        preferenceFlow.first()[preferenceEntry.key] ?: preferenceEntry.defaultValue

    override fun <T> getPreferenceFlow(request: PreferenceRequest<T>) = preferenceFlow.map {
        it[request.key] ?: request.defaultValue
    }.distinctUntilChanged()

    override suspend fun <T> editPreference(key: Preferences.Key<T>, newValue: T) {
        dataStore.edit { preferences -> preferences[key] = newValue }
    }

    override suspend fun clearPreferences() {
        dataStore.edit { preferences -> preferences.clear() }
    }
}