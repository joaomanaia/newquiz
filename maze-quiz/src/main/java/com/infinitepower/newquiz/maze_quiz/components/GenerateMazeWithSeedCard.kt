package com.infinitepower.newquiz.maze_quiz.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.data.worker.maze.GenerateMazeQuizWorker.Companion.GameModes
import kotlin.random.Random
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun GenerateMazeWithSeedCard(
    modifier: Modifier = Modifier,
    onClick: (seed: Int?, gameModesSelected: List<Int>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    GenerateMazeWithSeedCardImpl(
        modifier = modifier,
        onClick = onClick,
        expanded = expanded,
        onExpandClick = { expanded = !expanded }
    )
}

@Composable
@ExperimentalMaterial3Api
private fun GenerateMazeWithSeedCardImpl(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: (seed: Int?, gameModesSelected: List<Int>) -> Unit,
    onExpandClick: () -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small
    val spaceMedium = MaterialTheme.spacing.medium

    val (seed, setSeed) = remember {
        val randomSeed = Random.nextInt()
        mutableStateOf(randomSeed.toString())
    }

    val dropDownIcon = remember(expanded) {
        if (expanded) {
            Icons.Rounded.ArrowDropUp
        } else {
            Icons.Rounded.ArrowDropDown
        }
    }

    val allGameModes = remember {
        GameModes.values().toList()
    }

    val gameModesSelected = remember {
        val allGameModesIndex = allGameModes.indices.toList()
        mutableStateListOf(*allGameModesIndex.toTypedArray())
    }

    val generateButtonEnabled = remember(seed, gameModesSelected.size) {
        seed.isNotBlank() && gameModesSelected.isNotEmpty()
    }

    ElevatedCard(
        modifier = modifier,
        onClick = onExpandClick
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
                        text = stringResource(id = CoreR.string.custom_maze),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(spaceSmall))
                    Text(
                        text = stringResource(id = CoreR.string.generate_maze_with_custom_options),
                        style = MaterialTheme.typography.titleMedium
                    )

                }

                IconButton(onClick = onExpandClick) {
                    Icon(
                        imageVector = dropDownIcon,
                        contentDescription = stringResource(id = CoreR.string.expand_custom_options)
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Divider(modifier = Modifier.padding(vertical = spaceMedium))
                    GameModesComponent(
                        allGameModes = allGameModes,
                        gameModesSelected = gameModesSelected,
                        onSelectGameMode = { index ->
                            if (index in gameModesSelected) {
                                gameModesSelected.remove(index)
                            } else {
                                gameModesSelected.add(index)
                            }
                        }
                    )
                    Divider(modifier = Modifier.padding(vertical = spaceMedium))
                    OutlinedTextField(
                        value = seed,
                        onValueChange = setSeed,
                        label = { Text(text = stringResource(id = CoreR.string.seed)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(spaceMedium))

                    if (GameModes.MULTI_CHOICE.ordinal in gameModesSelected) {
                        Text(
                            text = stringResource(id = CoreR.string.custom_maze_multi_choice_warning),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(spaceMedium))
                    Button(
                        onClick = { onClick(seed.toIntOrNull(), gameModesSelected) },
                        modifier = Modifier.align(Alignment.End),
                        enabled = generateButtonEnabled
                    ) {
                        Text(text = stringResource(id = CoreR.string.generate))
                    }
                }
            }
        }
    }
}

@Composable
private fun GameModesComponent(
    modifier: Modifier = Modifier,
    allGameModes: List<GameModes>,
    gameModesSelected: List<Int>,
    onSelectGameMode: (index: Int) -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small

    Column(modifier = modifier) {
        Text(
            text = "Game Modes",
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(spaceSmall))
        allGameModes.forEachIndexed { index, mode ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = getGameModeText(gameMode = mode))
                Spacer(modifier = Modifier.weight(1f))
                Checkbox(
                    checked = index in gameModesSelected,
                    onCheckedChange = { onSelectGameMode(index) }
                )
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun getGameModeText(gameMode: GameModes) = when (gameMode) {
    GameModes.MULTI_CHOICE -> stringResource(id = CoreR.string.multi_choice_quiz)
    GameModes.LOGO -> stringResource(id = CoreR.string.logo_quiz)
    GameModes.FLAG -> stringResource(id = CoreR.string.flag_quiz)
    GameModes.WORDLE -> stringResource(id = CoreR.string.wordle)
    GameModes.GUESS_NUMBER -> stringResource(id = CoreR.string.guess_number)
    GameModes.GUESS_MATH_FORMULA -> stringResource(id = CoreR.string.guess_math_formula)
    GameModes.GUESS_MATH_SOLUTION -> stringResource(id = CoreR.string.guess_solution)
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun GenerateMazeWithSeedCardPreview() {
    NewQuizTheme {
        Surface {
            GenerateMazeWithSeedCardImpl(
                modifier = Modifier.padding(16.dp),
                onClick = { _, _ -> },
                expanded = true,
                onExpandClick = {}
            )
        }
    }
}