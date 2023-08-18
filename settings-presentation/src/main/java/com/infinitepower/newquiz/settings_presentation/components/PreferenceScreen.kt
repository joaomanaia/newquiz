package com.infinitepower.newquiz.settings_presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManagerImpl
import com.infinitepower.newquiz.settings_presentation.components.widgets.CustomPreferenceWidget
import com.infinitepower.newquiz.settings_presentation.model.Preference

/**
 * Preference Screen composable which contains a list of [Preference] items
 * @param items [Preference] items which should be displayed on the preference screen. An item can be a single [PreferenceItem] or a group ([PreferenceGroup])
 * @param dataStoreManager a [DataStoreManagerImpl] responsible for the dataStore backing the preference screen
 * @param modifier [Modifier] to be applied to the preferenceScreen layout
 */
@Composable
@ExperimentalMaterial3Api
internal fun PreferenceScreen(
    items: List<Preference>,
    dataStoreManager: DataStoreManager,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val prefs by dataStoreManager.preferenceFlow.collectAsState(initial = null)

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items.forEach { preference ->
            if (preference.visible) {
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

                    is Preference.CustomPreference -> {
                        item {
                            CustomPreferenceWidget(preference = preference)
                        }
                    }
                }
            }
        }
    }
}