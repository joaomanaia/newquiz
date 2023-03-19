package com.infinitepower.newquiz.core.common.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.infinitepower.newquiz.core.dataStore.manager.PreferenceRequest

val Context.comparisonQuizDataStore: DataStore<Preferences> by preferencesDataStore(name = "comparisonquiz")

object ComparisonQuizDataStoreCommon {
    object HighestPosition : PreferenceRequest<Int>(intPreferencesKey("highest_position"), 0)
}