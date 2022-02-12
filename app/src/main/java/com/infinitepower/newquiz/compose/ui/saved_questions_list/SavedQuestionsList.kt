package com.infinitepower.newquiz.compose.ui.saved_questions_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.android.material.snackbar.Snackbar
import com.infinitepower.newquiz.compose.core.common.UiEvent
import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.ui.components.dropdown_menu.DropdownMenu
import com.infinitepower.newquiz.compose.ui.components.dropdown_menu.DropdownMenuItem
import com.infinitepower.newquiz.compose.ui.saved_questions_list.components.SavedQuestionItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun SavedQuestionsList(
    navigator: DestinationsNavigator
) {
    val savedQuestionsListViewModel: SavedQuestionsListViewModel = hiltViewModel()

    val savedQuestions = savedQuestionsListViewModel.savedQuestions.collectAsLazyPagingItems()
    val selectedQuestions by savedQuestionsListViewModel.selectedQuestions.collectAsState()

    val context = LocalContext.current
    val localView = LocalView.current

    LaunchedEffect(key1 = true) {
        savedQuestionsListViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    val snackbar =
                        Snackbar.make(context, localView, event.message, Snackbar.LENGTH_LONG)
                    if (event.action != null) snackbar.setAction(event.action) {
                        savedQuestionsListViewModel.onEvent(SavedQuestionsListEvent.OnUndoDeleteClick)
                    }
                    snackbar.show()
                }
                is UiEvent.Navigate -> navigator.navigate(event.direction)
                is UiEvent.PopBackStack -> navigator.popBackStack()
                is UiEvent.RefreshData -> {
                    savedQuestions.refresh()
                }
            }
        }
    }

    var moreOptionsPopupExpanded by remember {
        mutableStateOf(false)
    }
    val changeMoreOptionsPopupVisibility = {
        moreOptionsPopupExpanded = !moreOptionsPopupExpanded
    }

    var sortPopupExpanded by remember {
        mutableStateOf(false)
    }
    val changeSortPopupVisibility = {
        sortPopupExpanded = !sortPopupExpanded
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Saved Questions")
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = changeSortPopupVisibility) {
                        Icon(
                            imageVector = Icons.Rounded.Sort,
                            contentDescription = "Sort"
                        )
                    }
                    IconButton(onClick = changeMoreOptionsPopupVisibility) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "More Options"
                        )
                    }

                    DropdownMenu(
                        expanded = moreOptionsPopupExpanded,
                        onDismissRequest = changeMoreOptionsPopupVisibility
                    ) {
                        val noQuestionsSelected = selectedQuestions.isEmpty()

                        DropdownMenuItem(
                            onClick = {
                                changeMoreOptionsPopupVisibility()
                                savedQuestionsListViewModel.onEvent(
                                    if (noQuestionsSelected) SavedQuestionsListEvent.OnSelectAllClick else SavedQuestionsListEvent.OnUnselectAllClick
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Rounded.SelectAll,
                                    contentDescription = if (noQuestionsSelected) "Select All" else "Unselect All"
                                )
                            }
                        ) {
                            Text(text = if (noQuestionsSelected) "Select All" else "Unselect All")
                        }
                        if (!noQuestionsSelected) {
                            DropdownMenuItem(
                                onClick = {
                                    changeMoreOptionsPopupVisibility()
                                    savedQuestionsListViewModel.onEvent(SavedQuestionsListEvent.OnDeleteAllSelectedClick)
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Delete All Selected"
                                    )
                                }
                            ) {
                                Text(text = "Delete All Selected")
                            }
                        }
                    }

                    DropdownMenu(
                        expanded = sortPopupExpanded,
                        onDismissRequest = changeSortPopupVisibility
                    ) {
                        DropdownMenuItem(onClick = {
                            changeSortPopupVisibility()
                            savedQuestionsListViewModel.onEvent(
                                SavedQuestionsListEvent.OnSortOrderChange(
                                    SavedQuestionsListSortOrder.BY_DESCRIPTION
                                )
                            )
                        }) {
                            Text(text = "Sort by description")
                        }
                        DropdownMenuItem(onClick = {
                            changeSortPopupVisibility()
                            savedQuestionsListViewModel.onEvent(
                                SavedQuestionsListEvent.OnSortOrderChange(
                                    SavedQuestionsListSortOrder.BY_TYPE
                                )
                            )
                        }) {
                            Text(text = "Sort by type")
                        }
                        DropdownMenuItem(onClick = {
                            changeSortPopupVisibility()
                            savedQuestionsListViewModel.onEvent(
                                SavedQuestionsListEvent.OnSortOrderChange(
                                    SavedQuestionsListSortOrder.BY_DIFFICULTY
                                )
                            )
                        }) {
                            Text(text = "Sort by difficulty")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedQuestions.isNotEmpty()) {
                FloatingActionButton(onClick = {
                    savedQuestionsListViewModel.onEvent(SavedQuestionsListEvent.OnPlayQuizGame)
                }) {
                    Icon(
                        imageVector = Icons.Rounded.PlayCircle,
                        contentDescription = "Start Game"
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (savedQuestions.loadState.append is LoadState.Error) {
                item {
                    Text(
                        text = (savedQuestions.loadState.append as LoadState.Error).error.localizedMessage
                            ?: "Error",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            if (savedQuestions.loadState.append is LoadState.Loading) {
                item { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            }

            items(
                items = savedQuestions,
                key = { it.id }
            ) { question ->
                if (question != null) {
                    SavedQuestionItem(
                        question = question,
                        checked = isQuestionSelected(question, selectedQuestions),
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .animateItemPlacement(),
                        onEvent = savedQuestionsListViewModel::onEvent
                    )
                }
            }
        }
    }
}

private fun isQuestionSelected(
    question: Question,
    selectedQuestions: List<Question>
) = selectedQuestions.contains(question)