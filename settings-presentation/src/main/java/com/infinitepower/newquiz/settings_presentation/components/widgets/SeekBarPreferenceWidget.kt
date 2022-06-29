package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.settings_presentation.components.widgets.TextPreferenceWidget
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
internal fun SeekBarPreferenceWidget(
    preference: Preference.PreferenceItem.SeekBarPreference,
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    val currentValue = remember(value) { mutableStateOf(value) }

    TextPreferenceWidget(
        preference = preference,
        summary = {
            PreferenceSummary(
                preference = preference,
                sliderValue = currentValue.value,
                onValueChange = { currentValue.value = it },
                onValueChangeEnd = { onValueChange(currentValue.value) }
            )
        }
    )
}

@Composable
private fun PreferenceSummary(
    preference: Preference.PreferenceItem.SeekBarPreference,
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeEnd: () -> Unit,
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    Column {
        preference.summary?.let { Text(text = it) }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = preference.valueRepresentation(sliderValue))
            Spacer(modifier = Modifier.width(16.dp))
            Slider(
                value = sliderValue,
                onValueChange = { if (preference.enabled) onValueChange(it) },
                valueRange = preference.valueRange,
                steps = preference.steps,
                onValueChangeFinished = onValueChangeEnd,
                enabled = isEnabled
            )
        }
    }
}