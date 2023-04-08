package com.infinitepower.newquiz.multi_choice_quiz.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun SkipQuestionDialog(
    userDiamonds: Int,
    onSkipClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = {
            Text(text = stringResource(id = CoreR.string.skip_question_q))
        },
        text = {
            Text(
                text = pluralStringResource(
                    id = CoreR.plurals.you_have_n_diamonds_skip_question_q,
                    count = userDiamonds,
                    formatArgs = arrayOf(userDiamonds)
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onSkipClick) {
                Text(text = stringResource(id = CoreR.string.skip))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissClick) {
                Text(text = stringResource(id = CoreR.string.close))
            }
        }
    )
}