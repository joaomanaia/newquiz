package com.infinitepower.newquiz.feature.settings.components.preferences.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.feature.settings.model.Preference

@Composable
internal fun CustomPreferenceWidget(
    preference: Preference.CustomPreference
) {
    Box(
        modifier = Modifier.semantics {
            contentDescription = preference.title
        },
        content = preference.content
    )
}

@Composable
@PreviewLightDark
private fun CustomPreferenceWidgetPreview() {
    NewQuizTheme {
        Surface {
            CustomPreferenceWidget(
                preference = Preference.CustomPreference(
                    title = "Custom Preference",
                    content = {
                        Text(text = "Custom Preference")
                    }
                )
            )
        }
    }
}
