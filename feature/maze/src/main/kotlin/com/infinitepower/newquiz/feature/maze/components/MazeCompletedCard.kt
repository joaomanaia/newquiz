package com.infinitepower.newquiz.feature.maze.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.icons.Trophy
import com.infinitepower.newquiz.core.R as CoreR

@Composable
internal fun MazeCompletedCard(
    onRestartClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            Icon(
                imageVector = Trophy,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                Text(
                    text =  stringResource(id = CoreR.string.maze_completed),
                    style = MaterialTheme.typography.titleMedium
                )
                Button(onClick = onRestartClick) {
                    Text(text = stringResource(id = CoreR.string.restart_maze))
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun MazeCompletedCardPreview() {
    NewQuizTheme {
        Surface {
            MazeCompletedCard(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth()
            )
        }
    }
}
