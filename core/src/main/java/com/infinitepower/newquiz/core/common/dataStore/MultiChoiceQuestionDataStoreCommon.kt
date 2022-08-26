package com.infinitepower.newquiz.core.common.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.infinitepower.newquiz.core.dataStore.manager.PreferenceRequest

val Context.multiChoiceCategoriesDataStore: DataStore<Preferences> by preferencesDataStore(name = "multiChoiceCategories")

object MultiChoiceQuestionDataStoreCommon {
    object RecentCategories : PreferenceRequest<String>(stringPreferencesKey("recentCategories"), "[]")
}