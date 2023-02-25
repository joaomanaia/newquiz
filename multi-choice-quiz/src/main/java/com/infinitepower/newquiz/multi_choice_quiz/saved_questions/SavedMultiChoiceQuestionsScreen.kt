package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.rememberMultiChoiceLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import com.infinitepower.newquiz.multi_choice_quiz.saved_questions.components.SavedQuestionItem
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun SavedMultiChoiceQuestionsScreen(
    navigator: DestinationsNavigator,
    savedQuestionsScreenNavigator: SavedQuestionsScreenNavigator,
    viewModel: SavedMultiChoiceQuestionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val multiChoiceQuizLoggingAnalytics = rememberMultiChoiceLoggingAnalytics()

    SavedMultiChoiceQuestionsScreenImpl(
        uiState = uiState,
        onBackClick = navigator::popBackStack,
        onEvent = viewModel::onEvent,
        playWithQuestions = { questions ->
            multiChoiceQuizLoggingAnalytics.logPlaySavedQuestions(questions.size)
            savedQuestionsScreenNavigator.navigateToQuickQuiz(ArrayList(questions))
        }
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("SavedMultiChoiceScreen")
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.saved_questions))
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = { BackIconButton(onClick = onBackClick) },
                actions = {
                    MoreVertPopup(
                        expanded = moreVertPopupExpanded,
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
                }
            )
        },
        bottomBar = {
            if (uiState.questions.isNotEmpty()) {
                BottomAppBar(
                    actions = {
                        TopBarActionButton(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "More options",
                            onClick = { setMoreVertPopupExpanded(true) }
                        )
                        if (uiState.questions.isNotEmpty()) {
                            TopBarActionButton(
                                imageVector = Icons.Rounded.Sort,
                                contentDescription = "Sort questions",
                                onClick = { playWithQuestions(uiState.randomQuestions()) }
                            )
                            TopBarActionButton(
                                imageVector = Icons.Rounded.Shuffle,
                                contentDescription = "Play quiz with random saved questions",
                                onClick = { playWithQuestions(uiState.randomQuestions()) }
                            )
                        }
                    },
                    floatingActionButton = {
                        if (uiState.selectedQuestions.isNotEmpty()) {
                            ExtendedFloatingActionButton(
                                text = {
                                    Text(text = "Play quiz")
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.PlayArrow,
                                        contentDescription = "Play quiz with selected questions"
                                    )
                                },
                                onClick = { playWithQuestions(uiState.arrayListSelectedQuestions) },
                                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                            )
                        }
                    }
                )
            }
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
                    modifier = Modifier.fillMaxWidth(),
                    question = question,
                    selected = selected,
                    onClick = {
                        onEvent(SavedMultiChoiceQuestionsUiEvent.SelectQuestion(question))
                    },
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
        if (questionsSelected) {
            DropdownMenuItem(
                text = { Text(text = "Unselect All") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.SelectAll,
                        contentDescription = "Unselect all"
                    )
                },
                onClick = onSelectAllClick
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Delete All")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete All"
                    )
                },
                onClick = onDeleteAllSelectedClick
            )
        } else {
            DropdownMenuItem(
                text = { Text(text = "Select All") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.SelectAll,
                        contentDescription = "Select All"
                    )
                },
                onClick = onSelectAllClick
            )
        }
        DropdownMenuItem(
            text = { Text(text = "Download questions") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Download,
                    contentDescription = "Download questions"
                )
            },
            onClick = onDownloadQuestionsClick
        )
    }
}

@Composable
@AllPreviewsNightLight
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