package com.infinitepower.newquiz.feature.settings.util.datastore

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.infinitepower.newquiz.core.datastore.common.settingsDataStore
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.PreferencesDatastoreManager

@Composable
fun rememberSettingsDataStoreManager(): DataStoreManager {
    val context = LocalContext.current
    val dataStore = context.settingsDataStore

    return remember {
        PreferencesDatastoreManager(dataStore)
    }
}
