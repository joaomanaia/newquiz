package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Reorder
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.saved.SortSavedQuestionsBy
import com.infinitepower.newquiz.multi_choice_quiz.saved_questions.components.SavedQuestionItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun SavedMultiChoiceQuestionsScreen(
    navigator: DestinationsNavigator,
    savedQuestionsScreenNavigator: SavedQuestionsScreenNavigator,
    viewModel: SavedMultiChoiceQuestionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val analyticsHelper = LocalAnalyticsHelper.current

    SavedMultiChoiceQuestionsScreenImpl(
        uiState = uiState,
        onBackClick = navigator::popBackStack,
        onEvent = viewModel::onEvent,
        playWithQuestions = { questions ->
            analyticsHelper.logEvent(AnalyticsEvent.MultiChoicePlaySavedQuestions(questions.size))
            savedQuestionsScreenNavigator.navigateToMultiChoiceQuiz(ArrayList(questions))
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
private fun SavedMultiChoiceQuestionsScreenImpl(
    uiState: SavedMultiChoiceQuestionsUiState,
    onBackClick: () -> Unit,
    playWithQuestions: (questions: List<MultiChoiceQuestion>) -> Unit,
    onEvent: (event: SavedMultiChoiceQuestionsUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val (moreVertPopupExpanded, setMoreVertPopupExpanded) = remember {
        mutableStateOf(false)
    }

    val (sortPopupExpanded, setSortPopupExpanded) = remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.saved_questions))
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    TopBarActionButton(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = stringResource(id = CoreR.string.more_options),
                        onClick = { setMoreVertPopupExpanded(true) }
                    )
                    MoreVertPopup(
                        expanded = moreVertPopupExpanded,
                        isQuestionsNotEmpty = uiState.questions.isNotEmpty(),
                        questionsSelected = uiState.selectedQuestions.isNotEmpty(),
                        onDismiss = { setMoreVertPopupExpanded(false) },
                        onSelectAllClick = {
                            onEvent(SavedMultiChoiceQuestionsUiEvent.SelectAll)
                            setMoreVertPopupExpanded(false)
                        },
                        onDeleteAllSelectedClick = {
                            onEvent(SavedMultiChoiceQuestionsUiEvent.DeleteAllSelected)
                            setMoreVertPopupExpanded(false)
                        },
                        onDownloadQuestionsClick = {
                            onEvent(SavedMultiChoiceQuestionsUiEvent.DownloadQuestions)
                            setMoreVertPopupExpanded(false)
                        }
                    )
                    if (uiState.questions.isNotEmpty()) {
                        TopBarActionButton(
                            imageVector = Icons.Rounded.Sort,
                            contentDescription = stringResource(id = CoreR.string.sort_questions),
                            onClick = { setSortPopupExpanded(true) }
                        )
                        SortPopup(
                            expanded = sortPopupExpanded,
                            onDismiss = { setSortPopupExpanded(false) },
                            onSortClick = { sortBy ->
                                setSortPopupExpanded(false)
                                onEvent(SavedMultiChoiceQuestionsUiEvent.SortQuestions(sortBy))
                            }
                        )
                        TopBarActionButton(
                            imageVector = Icons.Rounded.Shuffle,
                            contentDescription = stringResource(id = CoreR.string.play_with_random_saved_questions),
                            onClick = { playWithQuestions(uiState.randomQuestions()) }
                        )
                    }
                },
                floatingActionButton = {
                    if (uiState.selectedQuestions.isNotEmpty()) {
                        ExtendedFloatingActionButton(
                            text = { Text(text = stringResource(id = CoreR.string.play)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Rounded.PlayArrow,
                                    contentDescription = stringResource(id = CoreR.string.play_with_selected_questions)
                                )
                            },
                            onClick = {
                                // Ensure question size is below 50
                                val playQuestions = uiState.selectedQuestions.take(50)
                                playWithQuestions(playQuestions)
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            items(
                items = uiState.questions,
                key = { it.id }
            ) { question ->
                val selected = question in uiState.selectedQuestions

                SavedQuestionItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItemPlacement(),
                    question = question,
                    selected = selected,
                    onClick = {
                        onEvent(SavedMultiChoiceQuestionsUiEvent.SelectQuestion(question))
                    }
                )
            }
        }
    }
}



@Composable
private fun TopBarActionButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

@Composable
private fun MoreVertPopup(
    isQuestionsNotEmpty: Boolean,
    questionsSelected: Boolean,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSelectAllClick: () -> Unit,
    onDeleteAllSelectedClick: () -> Unit,
    onDownloadQuestionsClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        if (isQuestionsNotEmpty) {
            if (questionsSelected) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = CoreR.string.unselect_all)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.SelectAll,
                            contentDescription = stringResource(id = CoreR.string.unselect_all)
                        )
                    },
                    onClick = onSelectAllClick
                )
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(id = CoreR.string.delete_selected))
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(id = CoreR.string.delete_selected)
                        )
                    },
                    onClick = onDeleteAllSelectedClick
                )
            } else {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = CoreR.string.select_all)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.SelectAll,
                            contentDescription = stringResource(id = CoreR.string.select_all)
                        )
                    },
                    onClick = onSelectAllClick
                )
            }
        }
        DropdownMenuItem(
            text = { Text(text = stringResource(id = CoreR.string.download_questions)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Download,
                    contentDescription = stringResource(id = CoreR.string.download_questions)
                )
            },
            onClick = onDownloadQuestionsClick
        )
    }
}

@Composable
private fun SortPopup(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onSortClick: (sortBy: SortSavedQuestionsBy) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = CoreR.string.sort_by_default)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Reorder,
                    contentDescription = stringResource(id = CoreR.string.sort_by_default)
                )
            },
            onClick = { onSortClick(SortSavedQuestionsBy.BY_DEFAULT) }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = CoreR.string.sort_by_description)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Description,
                    contentDescription = stringResource(id = CoreR.string.sort_by_description)
                )
            },
            onClick = { onSortClick(SortSavedQuestionsBy.BY_DESCRIPTION) }
        )
        DropdownMenuItem(
            text = { Text(text = stringResource(id = CoreR.string.sort_by_category)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Category,
                    contentDescription = stringResource(id = CoreR.string.sort_by_category)
                )
            },
            onClick = { onSortClick(SortSavedQuestionsBy.BY_CATEGORY) }
        )
    }
}

@Composable
@PreviewScreenSizes
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun SavedMultiChoiceQuestionsScreenPreview() {
    val questions = List(10) {
        getBasicMultiChoiceQuestion()
    }

    NewQuizTheme {
        SavedMultiChoiceQuestionsScreenImpl(
            uiState = SavedMultiChoiceQuestionsUiState(
                questions = questions,
                selectedQuestions = questions.take(3)
            ),
            onBackClick = {},
            onEvent = {},
            playWithQuestions = {}
        )
    }
}