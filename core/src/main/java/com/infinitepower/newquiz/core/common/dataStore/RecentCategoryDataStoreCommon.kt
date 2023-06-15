package com.infinitepower.newquiz.core.common.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.infinitepower.newquiz.core.dataStore.manager.PreferenceRequest

val Context.recentCategoriesDataStore: DataStore<Preferences> by preferencesDataStore(name = "recent_categories")

object RecentCategoryDataStoreCommon {
    object MultiChoice : PreferenceRequest<Set<String>>(stringSetPreferencesKey("multi_choice"), emptySet())

    object Wordle : PreferenceRequest<Set<String>>(stringSetPreferencesKey("wordle"), emptySet())

    object ComparisonQuiz : PreferenceRequest<Set<String>>(stringSetPreferencesKey("comparison_quiz"), emptySet())
}
