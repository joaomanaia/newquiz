package com.infinitepower.newquiz.settings_presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManagerImpl
import com.infinitepower.newquiz.settings_presentation.model.Preference

/**
 * Preference Screen composable which contains a list of [Preference] items
 * @param items [Preference] items which should be displayed on the preference screen. An item can be a single [PreferenceItem] or a group ([PreferenceGroup])
 * @param dataStore a [DataStore] where the preferences will be saved
 * @param modifier [Modifier] to be applied to the preferenceScreen layout
 */
@Composable
@ExperimentalMaterial3Api
fun PreferenceScreen(
    items: List<Preference>,
    dataStore: DataStore<Preferences>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val dataStoreManager = remember {
        DataStoreManagerImpl(dataStore)
    }

    PreferenceScreen(
        items = items,
        modifier = modifier,
        dataStoreManager = dataStoreManager,
        contentPadding = contentPadding
    )
}

/**
 * Preference Screen composable which contains a list of [Preference] items
 * @param items [Preference] items which should be displayed on the preference screen. An item can be a single [PreferenceItem] or a group ([PreferenceGroup])
 * @param dataStoreManager a [DataStoreManagerImpl] responsible for the dataStore backing the preference screen
 * @param modifier [Modifier] to be applied to the preferenceScreen layout
 */
@Composable
@ExperimentalMaterial3Api
fun PreferenceScreen(
    items: List<Preference>,
    dataStoreManager: DataStoreManagerImpl,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val prefs by dataStoreManager.preferenceFlow.collectAsState(initial = null)

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items.forEach { preference ->
            when (preference) {
                // Create Preference Group
                is Preference.PreferenceGroup -> {
                    item {
                        PreferenceGroupHeader(title = preference.title)
                    }
                    items(preference.preferenceItems) { item ->
                        val enabled = preference.enabled && item.dependency.all { dependency ->
                            prefs?.get(dependency.key) ?: dependency.defaultValue
                        }

                        CompositionLocalProvider(LocalPreferenceEnabledStatus provides enabled) {
                            PreferenceItem(
                                item = item,
                                prefs = prefs,
                                dataStoreManager = dataStoreManager
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Create Preference Item
                is Preference.PreferenceItem<*> -> item {
                    val enabled = preference.enabled && preference.dependency.all { dependency ->
                        prefs?.get(dependency.key) ?: dependency.defaultValue
                    }

                    CompositionLocalProvider(LocalPreferenceEnabledStatus provides enabled) {
                        PreferenceItem(
                            item = preference,
                            prefs = prefs,
                            dataStoreManager = dataStoreManager
                        )
                    }
                }
            }
        }
    }
}