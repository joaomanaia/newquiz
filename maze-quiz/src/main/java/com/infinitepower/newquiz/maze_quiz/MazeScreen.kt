package com.infinitepower.newquiz.maze_quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.maze_quiz.components.GenerateMazeComponent
import com.infinitepower.newquiz.maze_quiz.components.MazeComponent
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.maze.MazeQuiz.MazeItem
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination(
    deepLinks = [
        DeepLink(uriPattern = "newquiz://maze")
    ]
)
@OptIn(ExperimentalMaterial3Api::class)
fun MazeScreen(
    navigator: DestinationsNavigator,
    mazeScreenNavigator: MazeScreenNavigator,
    viewModel: MazeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MazeScreenImpl(
        uiState = uiState,
        navigateBack = navigator::popBackStack,
        uiEvent = viewModel::onEvent,
        onItemClick = mazeScreenNavigator::navigateToGame
    )
}

@Composable
@ExperimentalMaterial3Api
private fun MazeScreenImpl(
    uiState: MazeScreenUiState,
    navigateBack: () -> Unit,
    uiEvent: (event: MazeScreenUiEvent) -> Unit,
    onItemClick: (item: MazeItem) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val clipboardManager = LocalClipboardManager.current

    val formulas = uiState.mathMaze.items

    var moreOptionsExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.maze))
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = CoreR.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { moreOptionsExpanded = true }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(id = CoreR.string.more_options)
                        )
                    }
                    DropdownMenu(
                        expanded = moreOptionsExpanded,
                        onDismissRequest = { moreOptionsExpanded = false }
                    ) {
                        if (formulas.isNotEmpty()) {
                            DropdownMenuItem(
                                text = { Text(stringResource(id = CoreR.string.copy_maze_seed)) },
                                onClick = {
                                    val mazeSeed = uiState.mazeSeed
                                    if (mazeSeed != null) {
                                        clipboardManager.setText(AnnotatedString(mazeSeed.toString()))
                                    }
                                    moreOptionsExpanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.ContentCopy,
                                        contentDescription = stringResource(id = CoreR.string.copy_maze_seed)
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(stringResource(id = CoreR.string.restart_maze)) },
                                onClick = {
                                    uiEvent(MazeScreenUiEvent.RestartMaze)
                                    moreOptionsExpanded = false
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.RestartAlt,
                                        contentDescription = stringResource(id = CoreR.string.restart_maze)
                                    )
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = spaceMedium)
        ) {
            if (uiState.loading) {
                CircularProgressIndicator()
            }

            if (!uiState.loading && uiState.isMazeEmpty) {
                GenerateMazeComponent(
                    modifier = Modifier.fillMaxSize(),
                    onGenerateClick = { seed, multiChoiceCategories, wordleCategories ->
                        uiEvent(MazeScreenUiEvent.GenerateMaze(seed, multiChoiceCategories, wordleCategories))
                    }
                )
            }

            if (formulas.isNotEmpty()) {
                MazeComponent(
                    modifier = Modifier.fillMaxSize(),
                    items = formulas,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
fun MazeScreenPreview() {
    val completedItems = List(9) {
        MazeItem.Wordle(
            wordleWord = WordleWord("1+1=2"),
            difficulty = QuestionDifficulty.Easy,
            played = true,
            wordleQuizType = WordleQuizType.MATH_FORMULA,
            mazeSeed = 0
        )
    }

    val otherItems = List(20) {
        MazeItem.Wordle(
            wordleWord = WordleWord("1+1=2"),
            difficulty = QuestionDifficulty.Easy,
            wordleQuizType = WordleQuizType.MATH_FORMULA,
            mazeSeed = 0
        )
    }

    val mazeItems = completedItems + otherItems

    NewQuizTheme {
        MazeScreenImpl(
            uiState = MazeScreenUiState(
                loading = false,
                mathMaze = MazeQuiz(items = mazeItems)
            ),
            navigateBack = {},
            uiEvent = {},
            onItemClick = {}
        )
    }
}