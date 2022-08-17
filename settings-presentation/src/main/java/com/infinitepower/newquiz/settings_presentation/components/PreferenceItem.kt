package com.infinitepower.newquiz.settings_presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.preferences.core.Preferences
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.settings_presentation.components.widgets.MultiSelectListPreferenceWidget
import com.infinitepower.newquiz.settings_presentation.components.widgets.*
import com.infinitepower.newquiz.settings_presentation.components.widgets.DropDownPreferenceWidget
import com.infinitepower.newquiz.settings_presentation.components.widgets.ListPreferenceWidget
import com.infinitepower.newquiz.settings_presentation.components.widgets.SeekBarPreferenceWidget
import com.infinitepower.newquiz.settings_presentation.components.widgets.SwitchPreferenceWidget
import com.infinitepower.newquiz.settings_presentation.model.Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterial3Api
internal fun PreferenceItem(
    item: Preference.PreferenceItem<*>,
    prefs: Preferences?,
    dataStoreManager: DataStoreManager
) {
    val scope = rememberCoroutineScope()

    when (item) {
        is Preference.PreferenceItem.SwitchPreference -> {
            SwitchPreferenceWidget(
                preference = item,
                value = prefs?.get(item.request.key) ?: item.request.defaultValue,
                onValueChange = { newValue ->
                    scope.launch(Dispatchers.IO) { dataStoreManager.editPreference(item.request.key, newValue) }
                }
            )
        }
        is Preference.PreferenceItem.ListPreference -> {
            ListPreferenceWidget(
                preference = item,
                value = prefs?.get(item.request.key) ?: item.request.defaultValue,
                onValueChange = { newValue ->
                    scope.launch(Dispatchers.IO) { dataStoreManager.editPreference(item.request.key, newValue) }
                }
            )
        }
        is Preference.PreferenceItem.SeekBarPreference -> {
            SeekBarPreferenceWidget(
                preference = item,
                value = prefs?.get(item.request.key) ?: item.request.defaultValue,
                onValueChange = { newValue ->
                    scope.launch(Dispatchers.IO) { dataStoreManager.editPreference(item.request.key, newValue) }
                }
            )
        }
        is Preference.PreferenceItem.DropDownMenuPreference -> {
            DropDownPreferenceWidget(
                preference = item,
                value = prefs?.get(item.request.key) ?: item.request.defaultValue,
                onValueChange = { newValue ->
                    scope.launch(Dispatchers.IO) { dataStoreManager.editPreference(item.request.key, newValue) }
                }
            )
        }
        is Preference.PreferenceItem.TextPreference -> {
            TextPreferenceWidget(
                preference = item,
                onClick = item.onClick
            )
        }
        is Preference.PreferenceItem.MultiSelectListPreference -> {
            MultiSelectListPreferenceWidget(
                preference = item,
                values = prefs?.get(item.request.key) ?: item.request.defaultValue,
                onValuesChange = { newValues ->
                    scope.launch(Dispatchers.IO) { dataStoreManager.editPreference(item.request.key, newValues) }
                }
            )
        }
    }
}