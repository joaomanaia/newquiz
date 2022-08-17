package com.infinitepower.newquiz.settings_presentation.components.widgets

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.settings_presentation.model.Preference

@Composable
@ExperimentalMaterial3Api
internal fun ListPreferenceWidget(
    preference: Preference.PreferenceItem.ListPreference,
    value: String,
    onValueChange: (String) -> Unit
) {
    val (isDialogShown, showDialog) = remember { mutableStateOf(false) }
    val dismissDialog = { showDialog(false) }

    val (newValue, setNewValue) = remember(value) {
        mutableStateOf(value)
    }

    TextPreferenceWidget(
        preference = preference,
        summary = preference.entries[value],
        onClick = { showDialog(!isDialogShown) },
    )

    if (isDialogShown) {
        AlertDialog(
            onDismissRequest = dismissDialog,
            title = { Text(text = preference.title) },
            text = {
                LazyColumn(modifier = Modifier.selectableGroup()) {
                    items(preference.entries.keys.toList()) { key ->
                        val isSelected = newValue == key
                        val onSelected = { setNewValue(key) }

                        SelectableListItem(
                            text = preference.entries[key].orEmpty(),
                            isSelected = isSelected,
                            onClick = onSelected
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dismissDialog()
                        onValueChange(newValue)
                    }
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = dismissDialog) {
                    Text(text = "Dismiss")
                }
            }
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun SelectableListItem(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        headlineText = { Text(text = text) },
        leadingContent = {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
            )
        },
        modifier = modifier.selectable(
            selected = isSelected,
            onClick = onClick
        )
    )
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun SelectableListItemPreview() {
    NewQuizTheme {
        Surface {
            SelectableListItem(
                text = "NewQuiz",
                isSelected = true,
                onClick = {}
            )
        }
    }
}