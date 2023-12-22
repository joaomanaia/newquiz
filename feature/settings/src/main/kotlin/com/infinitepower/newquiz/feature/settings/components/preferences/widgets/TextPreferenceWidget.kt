package com.infinitepower.newquiz.feature.settings.components.preferences.widgets

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.core.ui.StatusWrapper
import com.infinitepower.newquiz.feature.settings.model.Preference

@Composable
internal fun TextPreferenceWidget(
    preference: Preference.PreferenceItem<*>,
    summary: String? = null,
    onClick: () -> Unit = {},
    trailing: @Composable (() -> Unit)? = null
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    val summaryText = summary ?: preference.summary

    StatusWrapper(enabled = isEnabled) {
        if (summaryText == null) {
            ListItem(
                headlineContent = {
                    Text(
                        text = preference.title,
                        maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                    )
                },
                leadingContent = preference.icon,
                modifier = Modifier.clickable(
                    enabled = isEnabled,
                    onClick = onClick
                ),
                trailingContent = trailing,
            )
        } else {
            ListItem(
                headlineContent = {
                    Text(
                        text = preference.title,
                        maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                    )
                },
                supportingContent = {
                    Text(
                        text = summaryText,
                        maxLines = if (preference.singleLineSummary) 1 else Int.MAX_VALUE
                    )
                },
                leadingContent = preference.icon,
                modifier = Modifier.clickable(
                    enabled = isEnabled,
                    onClick = onClick
                ),
                trailingContent = trailing
            )
        }
    }
}

@Composable
internal fun TextPreferenceWidgetRes(
    preference: Preference.PreferenceItem<*>,
    summary: String? = null,
    onClick: () -> Unit = { },
    trailing: @Composable (() -> Unit)? = null
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    StatusWrapper(enabled = isEnabled) {
        ListItem(
            headlineContent = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                )
            },
            supportingContent = {
                val text = summary ?: preference.summary
                if (text != null) {
                    Text(
                        text = text,
                        maxLines = if (preference.singleLineSummary) 1 else Int.MAX_VALUE
                    )
                }
            },
            leadingContent = preference.icon,
            modifier = Modifier.clickable(
                enabled = isEnabled,
                onClick = onClick
            ),
            trailingContent = trailing,
        )
    }
}

@Composable
internal fun TextPreferenceWidget(
    preference: Preference.PreferenceItem<*>,
    summary: @Composable () -> Unit,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = { }
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    StatusWrapper(enabled = isEnabled) {
        ListItem(
            headlineContent = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = summary,
            leadingContent = preference.icon,
            modifier = Modifier.clickable(
                enabled = isEnabled,
                onClick = onClick
            ),
            trailingContent = trailing,
        )
    }
}
