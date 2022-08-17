package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
@ExperimentalMaterial3Api
internal fun SwitchPreferenceWidget(
    preference: Preference.PreferenceItem.SwitchPreference,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    TextPreferenceWidget(
        preference = preference,
        onClick = { onValueChange(!value) }
    ) {
        Switch(
            checked = value,
            onCheckedChange = onValueChange,
            enabled = preference.enabled
        )
    }
}