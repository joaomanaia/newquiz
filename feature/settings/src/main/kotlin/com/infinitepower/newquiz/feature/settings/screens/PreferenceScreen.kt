package com.infinitepower.newquiz.feature.settings.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.feature.settings.components.preferences.PreferenceGroupHeader
import com.infinitepower.newquiz.feature.settings.components.preferences.PreferenceItem
import com.infinitepower.newquiz.feature.settings.components.preferences.widgets.CustomPreferenceWidget
import com.infinitepower.newquiz.feature.settings.model.Preference
import kotlinx.collections.immutable.ImmutableList

@Composable
@ExperimentalMaterial3Api
fun PreferenceScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    title: String,
    items: ImmutableList<Preference>,
    dataStoreManager: DataStoreManager,
    isScreenExpanded: Boolean
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = title) },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    if (!isScreenExpanded) {
                        BackIconButton(onClick = onBackClick)
                    }
                }
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PreferenceScreen(
            modifier = Modifier.padding(innerPadding),
            items = items,
            dataStoreManager = dataStoreManager
        )
    }
}

/**
 * Preference Screen composable which contains a list of [Preference] items
 * @param items [Preference] items which should be displayed on the preference screen. An item can be a single [PreferenceItem] or a group ([Preference.PreferenceGroup])
 * @param dataStoreManager a [DataStoreManager] responsible for the dataStore backing the preference screen
 * @param modifier [Modifier] to be applied to the preferenceScreen layout
 */
@Composable
internal fun PreferenceScreen(
    modifier: Modifier = Modifier,
    items: ImmutableList<Preference>,
    dataStoreManager: DataStoreManager,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val prefs by dataStoreManager.preferenceFlow.collectAsStateWithLifecycle(initialValue = null)

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
