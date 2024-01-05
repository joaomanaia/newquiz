package com.infinitepower.newquiz.feature.maze.generate

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material.icons.rounded.SignalWifiConnectedNoInternet4
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.GameMode
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
@ExperimentalMaterial3Api
internal fun GenerateMazeScreen(
    onBackClick: () -> Unit,
    viewModel: GenerateMazeScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    GenerateMazeScreenImpl(
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
@ExperimentalMaterial3Api
internal fun GenerateMazeScreenImpl(
    uiState: GenerateMazeScreenUiState,
    onEvent: (event: GenerateMazeScreenUiEvent) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val showGenerateButton = remember(
        key1 = uiState.selectedMultiChoiceCategories.size,
        key2 = uiState.selectedWordleCategories.size
    ) {
        derivedStateOf {
            uiState.selectedMultiChoiceCategories.isNotEmpty() || uiState.selectedWordleCategories.isNotEmpty()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create Maze") },
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        },
        floatingActionButton = {
            if (showGenerateButton.value) {
                ExtendedFloatingActionButton(
                    onClick = {
                        onEvent(GenerateMazeScreenUiEvent.GenerateMaze(seed = null))
                    },
                ) {
                    Text(text = "Generate")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (uiState.loading || uiState.generatingMaze) {
                CircularProgressIndicator()
            } else {
                HelperChipsRow(
                    onSelectAllClick = {
                        onEvent(GenerateMazeScreenUiEvent.SelectAllCategories)
                    },
                    onOnlyOfflineClick = {
                        onEvent(GenerateMazeScreenUiEvent.SelectOnlyOfflineCategories)
                    }
                )
                CategoriesContent(
                    multiChoiceCategories = uiState.multiChoiceCategories,
                    selectedMultiChoiceCategories = uiState.selectedMultiChoiceCategories,
                    wordleCategories = uiState.wordleCategories,
                    selectedWordleCategories = uiState.selectedWordleCategories,
                    onEvent = onEvent
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CategoriesContent(
    multiChoiceCategories: ImmutableList<BaseCategory>,
    selectedMultiChoiceCategories: ImmutableList<BaseCategory>,
    wordleCategories: ImmutableList<BaseCategory>,
    selectedWordleCategories: ImmutableList<BaseCategory>,
    onEvent: (event: GenerateMazeScreenUiEvent) -> Unit
) {
    val multiChoiceParentBoxState = rememberParentBoxState(
        selectedCategories = selectedMultiChoiceCategories,
        categories = multiChoiceCategories
    )

    val wordleParentBoxState = rememberParentBoxState(
        selectedCategories = selectedWordleCategories,
        categories = wordleCategories
    )

    LazyColumn(
        contentPadding = PaddingValues(vertical = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        categoriesStickyHeader(
            title = "Multi Choice",
            parentBoxState = multiChoiceParentBoxState,
            onSelectAllClick = { selectAll ->
                onEvent(
                    GenerateMazeScreenUiEvent.SelectCategories(
                        gameMode = GameMode.MULTI_CHOICE,
                        selectAll = selectAll
                    )
                )
            }
        )

        categoriesItems(
            categories = multiChoiceCategories,
            selectedCategories = selectedMultiChoiceCategories,
            baseItemKey = "multi-choice",
            onSelectClick = { category ->
                onEvent(GenerateMazeScreenUiEvent.SelectCategory(category = category))
            }
        )

        categoriesStickyHeader(
            title = "Wordle",
            parentBoxState = wordleParentBoxState,
            onSelectAllClick = { selectAll ->
                onEvent(
                    GenerateMazeScreenUiEvent.SelectCategories(
                        gameMode = GameMode.WORDLE,
                        selectAll = selectAll
                    )
                )
            }
        )

        categoriesItems(
            categories = wordleCategories,
            selectedCategories = selectedWordleCategories,
            baseItemKey = "wordle",
            onSelectClick = { category ->
                onEvent(GenerateMazeScreenUiEvent.SelectCategory(category = category))
            }
        )
    }
}

@Composable
private fun <T : BaseCategory> rememberParentBoxState(
    selectedCategories: ImmutableList<T>,
    categories: ImmutableList<T>
): ToggleableState = remember(
    key1 = selectedCategories.size,
    key2 = categories.size
) {
    if (selectedCategories.isEmpty()) {
        ToggleableState.Off
    } else if (selectedCategories.size == categories.size) {
        ToggleableState.On
    } else {
        ToggleableState.Indeterminate
    }
}

/**
 * Creates a sticky header for the categories list with a parent checkbox.
 *
 * @param title the title of the sticky header
 * @param parentBoxState the state of the parent checkbox
 * @param onSelectAllClick called when the parent checkbox is clicked
 */
@ExperimentalFoundationApi
private fun LazyListScope.categoriesStickyHeader(
    title: String,
    parentBoxState: ToggleableState,
    onSelectAllClick: (selectAll: Boolean) -> Unit
) {
    stickyHeader {
        Surface(
            modifier = Modifier.fillParentMaxWidth(),
            checked = parentBoxState != ToggleableState.Off,
            onCheckedChange = onSelectAllClick,
        ) {
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    )
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium
                )
                TriStateCheckbox(
                    state = parentBoxState,
                    onClick = {
                        val selectAll = parentBoxState == ToggleableState.Off
                        onSelectAllClick(selectAll)
                    },
                )
            }
        }
    }
}

/**
 * Creates a list of categories.
 *
 * @param categories the list of categories
 * @param selectedCategories the list of selected categories
 * @param baseItemKey the base key for the items, it is used to generate the key for each game mode.
 */
private fun <T : BaseCategory> LazyListScope.categoriesItems(
    categories: ImmutableList<T>,
    selectedCategories: ImmutableList<T>,
    baseItemKey: String = "",
    onSelectClick: (category: T) -> Unit,
) {
    items(
        items = categories,
        key = { category -> "$baseItemKey-${category.id}" }
    ) { category ->
        CategoryComponent(
            title = category.name.asString(),
            imageUrl = category.image,
            onClick = { onSelectClick(category) },
            onCheckClick = { onSelectClick(category) },
            requireInternetConnection = category.requireInternetConnection,
            showConnectionInfo = ShowCategoryConnectionInfo.BOTH,
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            checked = category in selectedCategories,
        )
    }
}

@Composable
private fun HelperChipsRow(
    modifier: Modifier = Modifier,
    onSelectAllClick: () -> Unit,
    onOnlyOfflineClick: () -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.medium)
    ) {
        item {
            AssistChip(
                onClick = onSelectAllClick,
                label = {
                    Text(text = "Select All")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.SelectAll,
                        contentDescription = null
                    )
                },
            )
        }

        item {
            AssistChip(
                onClick = onOnlyOfflineClick,
                label = {
                    Text(text = "Select only Offline")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.SignalWifiConnectedNoInternet4,
                        contentDescription = null
                    )
                },
            )
        }
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun MazeScreenPreview() {
    val multiChoiceQuestionCategories = multiChoiceQuestionCategories.take(8).toImmutableList()
    val selectedMultiChoiceCategories = multiChoiceQuestionCategories.take(2).toImmutableList()

    val wordleCategories = WordleCategories.allCategories.toImmutableList()
    val selectedWordleCategories = wordleCategories.take(2).toImmutableList()

    NewQuizTheme {
        Surface {
            GenerateMazeScreenImpl(
                uiState = GenerateMazeScreenUiState(
                    multiChoiceCategories = multiChoiceQuestionCategories,
                    selectedMultiChoiceCategories = selectedMultiChoiceCategories,
                    wordleCategories = wordleCategories,
                    selectedWordleCategories = selectedWordleCategories,
                    loading = false
                )
            )
        }
    }
}
