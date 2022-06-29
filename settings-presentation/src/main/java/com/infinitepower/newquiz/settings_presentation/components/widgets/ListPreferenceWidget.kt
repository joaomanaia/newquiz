package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
internal fun ListPreferenceWidget(
    preference: Preference.PreferenceItem.ListPreference,
    value: String,
    onValueChange: (String) -> Unit
) {
    val (isDialogShown, showDialog) = remember { mutableStateOf(false) }

    TextPreferenceWidget(
        preference = preference,
        summary = value,
        onClick = { showDialog(!isDialogShown) },
    )

    if (isDialogShown) {
        AlertDialog(
            onDismissRequest = { showDialog(!isDialogShown) },
            title = { Text(text = preference.title) },
            text = {
                LazyColumn(modifier = Modifier.selectableGroup()) {
                    items(preference.entries.keys.toList()) { key ->
                        val isSelected = value == key
                        val onSelected = {
                            onValueChange(key)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    onClick = { if (!isSelected) onSelected() }
                                ).padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { if (!isSelected) onSelected() },
                            )
                            preference.entries[key]?.let { value ->
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = true
            ),
            confirmButton = {
                TextButton(
                    onClick = { showDialog(false) }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog(false) }
                ) {
                    Text(text = "Dismiss")
                }
            }
        )
    }
}