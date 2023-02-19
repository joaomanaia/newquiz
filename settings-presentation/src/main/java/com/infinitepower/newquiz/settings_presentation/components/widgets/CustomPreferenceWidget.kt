package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
fun CustomPreferenceWidget(
    preference: Preference.CustomPreference
) {
    Box(
        modifier = Modifier.semantics {
            contentDescription = preference.title
        },
        content = preference.content
    )
}