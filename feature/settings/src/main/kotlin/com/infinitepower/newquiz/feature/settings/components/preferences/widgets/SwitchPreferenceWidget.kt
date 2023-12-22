package com.infinitepower.newquiz.feature.settings.components.preferences.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.common.compose.preview.BooleanPreviewParameterProvider
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.core.datastore.PreferenceRequest
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.feature.settings.model.Preference

@Composable
internal fun SwitchPreferenceWidget(
    preference: Preference.PreferenceItem.SwitchPreference,
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    SwitchPreferenceContainer(
        checked = checked,
        isEnabled = isEnabled,
        isPrimary = preference.primarySwitch
    ) {
        TextPreferenceWidget(
            preference = preference,
            onClick = { onCheckChange(!checked) }
        ) {
            Switch(
                checked = checked,
                onCheckedChange = onCheckChange,
                enabled = isEnabled
            )
        }
    }
}

@Composable
private fun SwitchPreferenceContainer(
    checked: Boolean,
    isEnabled: Boolean,
    isPrimary: Boolean,
    content: @Composable () -> Unit
) {
    if (isPrimary) {
        val containerColor = if (checked) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }

        val tonalElevation = if (checked && isEnabled) 8.dp else 0.dp

        Surface(
            color = containerColor,
            tonalElevation = tonalElevation,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            content = content
        )
    } else {
        content()
    }
}

@Composable
@PreviewNightLight
private fun SwitchPreferencePreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) checked: Boolean
) {
    NewQuizTheme {
        Surface {
            SwitchPreferenceWidget(
                preference = Preference.PreferenceItem.SwitchPreference(
                    request = PreferenceRequest(
                        key = booleanPreferencesKey("switch_preference"),
                        defaultValue = false
                    ),
                    title = "Switch Preference",
                    enabled = true,
                    primarySwitch = true
                ),
                checked = checked,
                onCheckChange = {}
            )
        }
    }
}
