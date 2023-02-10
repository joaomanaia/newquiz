package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.rememberMultiChoiceLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

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
                    if (uiState.questions.isNotEmpty()) {
                        TopBarActionButton(
                            imageVector = Icons.Rounded.Shuffle,
                            contentDescription = "Play quiz with random saved questions",
                            onClick = { playWithQuestions(uiState.randomQuestions()) }
                        )
                    }
                    TopBarActionButton(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "More options",
                        onClick = { setMoreVertPopupExpanded(true) }
                    )
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
                    onClick = { playWithQuestions(uiState.arrayListSelectedQuestions) }
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            items(
                items = uiState.questions,
                key = { it.id }
            ) { question ->
                val selected = question in uiState.selectedQuestions

                QuestionItem(
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
@OptIn(ExperimentalMaterial3Api::class)
private fun QuestionItem(
    modifier: Modifier = Modifier,
    question: MultiChoiceQuestion,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        selected = selected,
        tonalElevation = if (selected) 8.dp else 0.dp,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {
            Text(
                text = question.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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
@PreviewNightLight
private fun SavedMultiChoiceQuestionsScreenPreview() {
    NewQuizTheme {
        SavedMultiChoiceQuestionsScreenImpl(
            uiState = SavedMultiChoiceQuestionsUiState(
                questions = List(10) {
                    getBasicMultiChoiceQuestion()
                }
            ),
            onBackClick = {},
            onEvent = {},
            playWithQuestions = {}
        )
    }
}