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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.data.worker.maze.GenerateMazeQuizWorker
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory

@Composable
@ExperimentalMaterial3Api
internal fun GenerateMazeComponent(
    modifier: Modifier = Modifier,
    onGenerateClick: (
        seed: Int?,
        selectedMultiChoiceCategories: List<MultiChoiceCategory>,
        selectedWordleCategories: List<WordleCategory>
    ) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceExtraLarge = MaterialTheme.spacing.extraLarge

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spaceMedium)
    ) {
        item {
            Text(
                text = stringResource(id = CoreR.string.generate_maze),
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(vertical = spaceExtraLarge)
            )
        }

        item {
            GenerateMazeCard(
                onClick = {
                    val allMultiChoiceCategories = GenerateMazeQuizWorker.GameModes.MultiChoice.categories
                    val allWordleCategories = GenerateMazeQuizWorker.GameModes.Wordle.categories
                    onGenerateClick(null, allMultiChoiceCategories, allWordleCategories)
                }
            )
        }

        item {
            GenerateOfflineMazeCard(
                onClick = {
                    val offlineMultiChoiceCategories = GenerateMazeQuizWorker.GameModes.MultiChoice.getOfflineCategories()
                    val offlineWordleCategories = GenerateMazeQuizWorker.GameModes.Wordle.getOfflineCategories()
                    onGenerateClick(null, offlineMultiChoiceCategories, offlineWordleCategories)
                }
            )
        }

        item {
            GenerateCustomMaze(onClick = onGenerateClick)
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
                text = stringResource(id = CoreR.string.random_maze),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(spaceSmall))
            Text(
                text = stringResource(id = CoreR.string.generate_maze_with_random_items),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun GenerateMazeComponentPreview() {
    NewQuizTheme {
        Surface {
            GenerateMazeComponent(
                modifier = Modifier.padding(16.dp),
                onGenerateClick = { _, _, _ -> }
            )
        }
    }
}

@Composable
@PreviewLightDark
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