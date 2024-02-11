package com.infinitepower.newquiz.core.ui.components.skip_question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
fun SkipQuestionDialog(
    userDiamonds: Int,
    skipCost: Int,
    loading: Boolean = false,
    onSkipClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    if (loading) {
        LoadingDialog()
    } else if (userDiamonds >= 0) {
        // If user diamonds is negative, it means that the user has already skipped the question or
        // the user didn't request to skip the question yet.
        SkipQuestionDialog(
            userDiamonds = userDiamonds,
            skipCost = skipCost,
            onSkipClick = onSkipClick,
            onDismissClick = onDismissClick
        )
    }
}

@Composable
private fun SkipQuestionDialog(
    userDiamonds: Int,
    skipCost: Int,
    onSkipClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val canSkip = remember(userDiamonds, skipCost) {
        userDiamonds >= skipCost
    }

    if (canSkip) {
        AlertDialog(
            onDismissRequest = onDismissClick,
            title = { Text(text = stringResource(id = CoreR.string.skip_question_q)) },
            text = {
                Text(
                    text = pluralStringResource(
                        id = CoreR.plurals.you_have_n_diamonds_skip_question_q,
                        count = userDiamonds,
                        formatArgs = arrayOf(userDiamonds, skipCost)
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSkipClick()
                        onDismissClick()
                    }
                ) {
                    Text(text = stringResource(id = CoreR.string.skip))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissClick) {
                    Text(text = stringResource(id = CoreR.string.close))
                }
            }
        )
    } else {
        NoDiamondsDialog(onDismissClick = onDismissClick)
    }
}

@Composable
private fun NoDiamondsDialog(
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = { Text(text = stringResource(id = R.string.no_diamonds)) },
        text = {
            Text(
                text = stringResource(id = R.string.no_diamonds_to_skip_question_description)
            )
        },
        confirmButton = {
            TextButton(onClick = onDismissClick) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}

@Composable
@ExperimentalMaterial3Api
private fun LoadingDialog() {
    BasicAlertDialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Loading your diamonds...",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                CircularProgressIndicator()
            }
        }
    }
}
