package com.infinitepower.newquiz.multi_choice_quiz.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.R

@Composable
internal fun NoDiamondsDialog(
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = {
            Text(text = stringResource(id = R.string.no_diamonds))
        },
        confirmButton = {
            TextButton(onClick = onDismissClick) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}