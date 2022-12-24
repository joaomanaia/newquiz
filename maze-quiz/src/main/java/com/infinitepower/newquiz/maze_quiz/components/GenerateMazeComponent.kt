package com.infinitepower.newquiz.maze_quiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing

@Composable
@ExperimentalMaterial3Api
internal fun GenerateMazeComponent(
    modifier: Modifier = Modifier,
    onGenerateClick: (
        seed: Int?,
        gameModesSelected: List<Int>?
    ) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceExtraLarge = MaterialTheme.spacing.extraLarge

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Generate Maze",
                style = MaterialTheme.typography.displaySmall
            )
        }

        item {
            Spacer(modifier = Modifier.height(spaceExtraLarge))
            GenerateMazeCard(
                onClick = {
                    onGenerateClick(null, null) // empty list means all game modes selected
                }
            )
            Spacer(modifier = Modifier.height(spaceMedium))
        }

        item {
            GenerateMazeWithSeedCard(onClick = onGenerateClick)
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun GenerateMazeCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small
    val spaceMedium = MaterialTheme.spacing.medium

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spaceMedium)
        ) {
            Text(
                text = "Random Maze",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(spaceSmall))
            Text(
                text = "Generate maze with random questions, all game modes will be included.",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun GenerateMazeComponentPreview() {
    NewQuizTheme {
        Surface {
            GenerateMazeComponent(
                modifier = Modifier.padding(16.dp),
                onGenerateClick = { _, _ -> }
            )
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun GenerateMazeCardPreview() {
    NewQuizTheme {
        Surface {
            GenerateMazeCard(
                modifier = Modifier.padding(16.dp),
                onClick = {}
            )
        }
    }
}