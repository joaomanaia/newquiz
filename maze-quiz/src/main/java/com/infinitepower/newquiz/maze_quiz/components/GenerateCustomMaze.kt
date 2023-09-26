package com.infinitepower.newquiz.maze_quiz.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
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
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.data.worker.maze.GenerateMazeQuizWorker.GameModes
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory
import kotlin.random.Random
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@ExperimentalMaterial3Api
internal fun GenerateCustomMaze(
    modifier: Modifier = Modifier,
    onClick: (
        seed: Int?,
        selectedMultiChoiceCategories: List<MultiChoiceCategory>,
        selectedWordleCategories: List<WordleCategory>
    ) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    GenerateCustomMaze(
        modifier = modifier,
        onClick = onClick,
        expanded = expanded,
        onExpandClick = { expanded = !expanded }
    )
}

@Composable
@ExperimentalMaterial3Api
private fun GenerateCustomMaze(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onClick: (
        seed: Int?,
        selectedMultiChoiceCategories: List<MultiChoiceCategory>,
        selectedWordleCategories: List<WordleCategory>
    ) -> Unit,
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

    val selectedMultiChoiceCategories = remember {
        GameModes.MultiChoice.categories.toMutableStateList()
    }

    val selectedWordleCategories = remember {
        GameModes.Wordle.categories.toMutableStateList()
    }

    val generateButtonEnabled = remember(
        seed,
        selectedMultiChoiceCategories.size,
        selectedWordleCategories.size
    ) {
        seed.isNotBlank() && (selectedMultiChoiceCategories.size > 0 || selectedWordleCategories.size > 0)
    }

    val showMultiChoiceSeedWarning by remember(selectedMultiChoiceCategories) {
        derivedStateOf {
            MultiChoiceBaseCategory.Normal().categoryId in selectedMultiChoiceCategories.map { category ->
                category.id
            }
        }
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
                        selectedMultiChoiceCategories = selectedMultiChoiceCategories,
                        onMultiChoiceCategoryClicked = { category ->
                            if (category in selectedMultiChoiceCategories) {
                                selectedMultiChoiceCategories.remove(category)
                            } else {
                                selectedMultiChoiceCategories.add(category)
                            }
                        },
                        onMultiChoiceParentClicked = { enableAll ->
                            selectedMultiChoiceCategories.clear()

                            if (enableAll) {
                                selectedMultiChoiceCategories.addAll(GameModes.MultiChoice.categories)
                            }
                        },
                        selectedWordleCategories = selectedWordleCategories,
                        onWordleCategoryClicked = { category ->
                            if (category in selectedWordleCategories) {
                                selectedWordleCategories.remove(category)
                            } else {
                                selectedWordleCategories.add(category)
                            }
                        },
                        onWordleParentClicked = { enableAll ->
                            selectedWordleCategories.clear()

                            if (enableAll) {
                                selectedWordleCategories.addAll(GameModes.Wordle.categories)
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
                    if (showMultiChoiceSeedWarning) {
                        Text(
                            text = stringResource(id = CoreR.string.custom_maze_multi_choice_warning),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(spaceMedium))
                    Button(
                        onClick = {
                            onClick(
                                seed.toIntOrNull(),
                                selectedMultiChoiceCategories,
                                selectedWordleCategories
                            )
                        },
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
    selectedMultiChoiceCategories: List<MultiChoiceCategory>,
    onMultiChoiceCategoryClicked: (category: MultiChoiceCategory) -> Unit,
    onMultiChoiceParentClicked: (enableAll: Boolean) -> Unit,
    selectedWordleCategories: List<WordleCategory>,
    onWordleCategoryClicked: (category: WordleCategory) -> Unit,
    onWordleParentClicked: (enableAll: Boolean) -> Unit
) {
    val spaceSmall = MaterialTheme.spacing.small

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = CoreR.string.categories),
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = Modifier.height(spaceSmall))
        GameModeComponent(
            gameMode = GameModes.MultiChoice,
            selectedCategories = selectedMultiChoiceCategories,
            onSelectChange = onMultiChoiceCategoryClicked,
            onParentSelectChange = onMultiChoiceParentClicked
        )
        Spacer(modifier = Modifier.height(spaceSmall))
        GameModeComponent(
            gameMode = GameModes.Wordle,
            selectedCategories = selectedWordleCategories,
            onSelectChange = onWordleCategoryClicked,
            onParentSelectChange = onWordleParentClicked
        )
    }
}

@Composable
internal fun <T : BaseCategory> GameModeComponent(
    modifier: Modifier = Modifier,
    gameMode: GameModes<T>,
    selectedCategories: List<T>,
    onSelectChange: (category: T) -> Unit,
    onParentSelectChange: (enableAll: Boolean) -> Unit
) {
    val parentBoxState = remember(selectedCategories.size) {
        if (selectedCategories.isEmpty()) {
            ToggleableState.Off
        } else if (selectedCategories.size == gameMode.categories.size) {
            ToggleableState.On
        } else {
            ToggleableState.Indeterminate
        }
    }

    val gameModeName = gameMode.name.asString()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(
                    value = parentBoxState == ToggleableState.On,
                    onValueChange = { onParentSelectChange(it) },
                    role = Role.Checkbox
                )
                .semantics(mergeDescendants = true) {
                    contentDescription = "$gameModeName game mode"
                }
        ) {
            TriStateCheckbox(
                state = parentBoxState,
                onClick = {
                    if (parentBoxState == ToggleableState.On) {
                        onParentSelectChange(false)
                    } else {
                        onParentSelectChange(true)
                    }
                },
                modifier = Modifier.semantics {
                    contentDescription = "$gameModeName game mode checkbox"

                    stateDescription = when (parentBoxState) {
                        ToggleableState.On -> "All categories selected"
                        ToggleableState.Off -> "No categories selected"
                        ToggleableState.Indeterminate -> "Some categories selected"
                    }
                }
            )
            Text(text = gameModeName)
        }
        gameMode.categories.forEach { category ->
            CategoryComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.medium),
                text = category.name.asString(),
                selected = category in selectedCategories,
                onSelectChange = { onSelectChange(category) }
            )
        }
    }
}

@Composable
private fun CategoryComponent(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelectChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = modifier
            .toggleable(
                value = selected,
                onValueChange = onSelectChange,
                role = Role.Checkbox
            )
            .semantics(mergeDescendants = true) {
                contentDescription = "$text category"
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = selected,
            onCheckedChange = onSelectChange,
            enabled = enabled,
            modifier = Modifier.semantics {
                contentDescription = "$text category checkbox"

                stateDescription = if (selected) {
                    "Selected"
                } else {
                    "Not selected"
                }
            }
        )
        Text(text = text)
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun GenerateCustomMazePreview() {
    NewQuizTheme {
        Surface {
            GenerateCustomMaze(
                modifier = Modifier.padding(16.dp),
                onClick = { _, _, _ -> },
                expanded = true,
                onExpandClick = {}
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true,
    group = "GameModeComponent",
)
private fun GameModeComponentPreview() {
    NewQuizTheme {
        Surface {
            GameModeComponent(
                modifier = Modifier.padding(16.dp),
                gameMode = GameModes.MultiChoice,
                selectedCategories = GameModes.MultiChoice.categories.shuffled().take(2),
                onSelectChange = {},
                onParentSelectChange = {}
            )
        }
    }
}
