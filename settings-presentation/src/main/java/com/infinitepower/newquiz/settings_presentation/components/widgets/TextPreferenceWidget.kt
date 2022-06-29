package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.compose.preferences.LocalPreferenceEnabledStatus
import com.infinitepower.newquiz.core.ui.StatusWrapper
import com.infinitepower.newquiz.settings_presentation.components.MaterialListItem
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
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
            MaterialListItem(
                text = {
                    Text(
                        text = preference.title,
                        maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                    )
                },
                icon = preference.icon,
                modifier = Modifier.clickable(
                    enabled = isEnabled,
                    onClick = onClick
                ),
                trailing = trailing,
            )
        } else {
            MaterialListItem(
                text = {
                    Text(
                        text = preference.title,
                        maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                    )
                },
                secondaryText = {
                    Text(text = summaryText)
                },
                icon = preference.icon,
                modifier = Modifier.clickable(
                    enabled = isEnabled,
                    onClick = onClick
                ),
                trailing = trailing,
            )
        }
    }
}

@Composable
internal fun TextPreferenceWidgetRes(
    preference: Preference.PreferenceItem<*>,
    @StringRes summary: String? = null,
    onClick: () -> Unit = { },
    trailing: @Composable (() -> Unit)? = null
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    StatusWrapper(enabled = isEnabled) {
        MaterialListItem(
            text = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
                )
            },
            secondaryText = {
                val text = summary ?: preference.summary
                if (text != null) Text(text = text)
            },
            icon = preference.icon,
            modifier = Modifier.clickable(
                enabled = isEnabled,
                onClick = onClick
            ),
            trailing = trailing,
        )
    }
}

@Composable
fun TextPreferenceWidget(
    preference: Preference.PreferenceItem<*>,
    summary: @Composable () -> Unit,
    onClick: () -> Unit = { },
    trailing: @Composable (() -> Unit)? = null
) {
    val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

    StatusWrapper(enabled = isEnabled) {
        MaterialListItem(
            text = {
                Text(
                    text = preference.title,
                    maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            secondaryText = summary,
            icon = preference.icon,
            modifier = Modifier.clickable(
                enabled = isEnabled,
                onClick = onClick
            ),
            trailing = trailing,
        )
    }
}