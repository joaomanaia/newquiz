package com.infinitepower.newquiz.home_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.theme.spacing

@Composable
internal fun SignInCard(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(spaceMedium),
        ) {
            Text(
                text = stringResource(id = R.string.save_you_progress_description),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spaceMedium)
            ) {
                TextButton(onClick = onDismissClick) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
                FilledTonalButton(onClick = onSignInClick) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
            }
        }
    }
}
