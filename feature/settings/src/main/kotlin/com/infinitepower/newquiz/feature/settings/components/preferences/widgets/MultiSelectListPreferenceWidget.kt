package com.infinitepower.newquiz.feature.settings.components.preferences.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.feature.settings.model.Preference

@Composable
internal fun MultiSelectListPreferenceWidget(
    preference: Preference.PreferenceItem.MultiSelectListPreference,
    values: Set<String>,
    onValuesChange: (Set<String>) -> Unit
) {
    val (isDialogShown, showDialog) = remember { mutableStateOf(false) }

    val description = preference
        .entries
        .filter { values.contains(it.key) }
        .map { it.value }
        .joinToString(separator = ", ", limit = 3)

    TextPreferenceWidget(
        preference = preference,
        summary = description.ifBlank { null },
        onClick = { showDialog(!isDialogShown) }
    )

    if (isDialogShown) {
        AlertDialog(
            onDismissRequest = { showDialog(false) },
            title = { Text(text = preference.title) },
            text = {
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
                        .verticalScroll(state = rememberScrollState())
                ) {
                    preference.entries.forEach { current ->
                        val isSelected = values.contains(current.key)
                        val onSelectionChanged = {
                            val result = when (!isSelected) {
                                true -> values + current.key
                                false -> values - current.key
                            }
                            onValuesChange(result)
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    onClick = { onSelectionChanged() }
                                )
                                .padding(4.dp)
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { onSelectionChanged() }
                            )
                            Text(
                                text = current.value,
                                style = MaterialTheme.typography.bodyLarge.merge(),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showDialog(false) },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary),
                ) {
                    Text(text = stringResource(android.R.string.ok))
                }
            }
        )
    }
}