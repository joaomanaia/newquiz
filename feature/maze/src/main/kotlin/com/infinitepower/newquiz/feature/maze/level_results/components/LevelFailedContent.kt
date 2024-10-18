package com.infinitepower.newquiz.feature.maze.level_results.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun LevelFailedContent(
    onRetry: () -> Unit,
    popBackStack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.large)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = CoreR.string.level_failed),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.error
        )
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(MaterialTheme.spacing.large)
                .background(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = CircleShape
                ).padding(MaterialTheme.spacing.medium),
            tint = MaterialTheme.colorScheme.error
        )
        Text(
            text = stringResource(id = CoreR.string.level_failed_description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = MaterialTheme.spacing.large)
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = CoreR.string.try_again))
        }
        TextButton(
            onClick = popBackStack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Rounded.Home,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Text(text = stringResource(id = CoreR.string.main_menu))
        }
    }
}

@Composable
@PreviewLightDark
private fun LevelFailedContentPreview() {
    NewQuizTheme {
        Surface {
            LevelFailedContent(
                onRetry = {},
                popBackStack = {}
            )
        }
    }
}
