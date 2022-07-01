package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.core.ui.StatusWrapper
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TextPreferenceWidget(
    preference: Preference.PreferenceItem<*>,
    summary: String? = null,
    onClick: () -> Unit = { },
    trailing: @Composable (() -> Unit)? = null
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    val summaryText = summary ?: preference.summary

    StatusWrapper(enabled = isEnabled) {
        if (summaryText == null) {
            ListItem(
                headlineText = {
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
                headlineText = {
                    Text(
                        text = preference.title,
                        maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                    )
                },
                supportingText = {
                    Text(text = summaryText)
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
@OptIn(ExperimentalMaterial3Api::class)
internal fun TextPreferenceWidgetRes(
    preference: Preference.PreferenceItem<*>,
    summary: String? = null,
    onClick: () -> Unit = { },
    trailing: @Composable (() -> Unit)? = null
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    StatusWrapper(enabled = isEnabled) {
        ListItem(
            headlineText = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                )
            },
            supportingText = {
                val text = summary ?: preference.summary
                if (text != null) Text(text = text)
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
@OptIn(ExperimentalMaterial3Api::class)
internal fun TextPreferenceWidget(
    preference: Preference.PreferenceItem<*>,
    summary: @Composable () -> Unit,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit = { }
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    StatusWrapper(enabled = isEnabled) {
        ListItem(
            headlineText = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingText = summary,
            leadingContent = preference.icon,
            modifier = Modifier.clickable(
                enabled = isEnabled,
                onClick = onClick
            ),
            trailingContent = trailing,
        )
    }
}