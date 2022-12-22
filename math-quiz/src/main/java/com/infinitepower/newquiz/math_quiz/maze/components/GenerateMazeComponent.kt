package com.infinitepower.newquiz.math_quiz.maze.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import kotlin.random.Random

@Composable
@ExperimentalMaterial3Api
internal fun GenerateMazeComponent(
    modifier: Modifier = Modifier,
    onGenerateClick: (seed: Int?) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium
    val spaceExtraLarge = MaterialTheme.spacing.extraLarge

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Generate Maze",
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(spaceExtraLarge))
        GenerateMazeCard(onClick = { onGenerateClick(null) })
        Spacer(modifier = Modifier.height(spaceMedium))
        GenerateMazeWithSeedCard(onClick = { onGenerateClick(it) })
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
                text = "Generate maze with random formulas",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun GenerateMazeWithSeedCard(
    modifier: Modifier = Modifier,
    onClick: (seed: Int?) -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small
    val spaceMedium = MaterialTheme.spacing.medium

    var expanded by remember { mutableStateOf(false) }
    val (seed, setSeed) = remember { mutableStateOf("") }

    val dropDownIcon = remember(expanded) {
        if (expanded) {
            Icons.Rounded.ArrowDropUp
        } else {
            Icons.Rounded.ArrowDropDown
        }
    }

    ElevatedCard(
        modifier = modifier,
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spaceMedium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Custom Maze",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(spaceSmall))
                    Text(
                        text = "Generate maze with custom options",
                        style = MaterialTheme.typography.titleMedium
                    )

                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = dropDownIcon,
                        contentDescription = "Expand custom options"
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(spaceMedium))
                    OutlinedTextField(
                        value = seed,
                        onValueChange = setSeed,
                        label = { Text(text = "Seed") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(spaceMedium))
                    Button(
                        onClick = { onClick(seed.toIntOrNull()) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Generate")
                    }
                }
            }
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
                onGenerateClick = {}
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

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun GenerateMazeWithSeedCardPreview() {
    NewQuizTheme {
        Surface {
            GenerateMazeWithSeedCard(
                modifier = Modifier.padding(16.dp),
                onClick = {}
            )
        }
    }
}