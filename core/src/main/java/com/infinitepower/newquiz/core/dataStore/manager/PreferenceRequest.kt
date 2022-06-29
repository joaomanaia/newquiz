package com.infinitepower.newquiz.core.dataStore.manager

import androidx.datastore.preferences.core.Preferences

open class PreferenceRequest<T>(
    val key: Preferences.Key<T>,
    val defaultValue: T
)