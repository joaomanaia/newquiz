package com.infinitepower.newquiz.feature.settings.components.preferences.widgets

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.feature.settings.model.Preference
import com.infinitepower.newquiz.core.R as CoreR

@Composable
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
                        preference.onItemClick(newValue)
                        onValueChange(newValue)
                    }
                ) {
                    Text(text = stringResource(id = CoreR.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = dismissDialog) {
                    Text(text = stringResource(id = CoreR.string.dismiss))
                }
            }
        )
    }
}

@Composable
private fun SelectableListItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean
) {
    ListItem(
        headlineContent = { Text(text = text) },
        leadingContent = {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
            )
        },
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
@PreviewLightDark
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
