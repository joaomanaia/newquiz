package com.infinitepower.newquiz.feature.maze

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.feature.maze.components.MazeCompletedCard
import com.infinitepower.newquiz.feature.maze.components.MazePath
import com.infinitepower.newquiz.feature.maze.generate.GenerateMazeScreen
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.maze.MazeQuiz.MazeItem
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.toPersistentList
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
    when {
        uiState.loading -> {
            Box {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
        !uiState.loading && uiState.isMazeEmpty -> GenerateMazeScreen(onBackClick = navigateBack)
        !uiState.loading && !uiState.isMazeEmpty -> MazePathScreen(
            uiState = uiState,
            navigateBack = navigateBack,
            uiEvent = uiEvent,
            onItemClick = onItemClick
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun MazePathScreen(
    uiState: MazeScreenUiState,
    navigateBack: () -> Unit,
    uiEvent: (event: MazeScreenUiEvent) -> Unit,
    onItemClick: (item: MazeItem) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current

    var moreOptionsExpanded by remember { mutableStateOf(false) }

    val spaceMedium = MaterialTheme.spacing.medium

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.maze))
                },
                navigationIcon = { BackIconButton(onClick = navigateBack) },
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
                        if (!uiState.isMazeEmpty) {
                            uiState.mazeSeed?.let { mazeSeed ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(id = CoreR.string.copy_maze_seed)) },
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(mazeSeed.toString()))
                                        moreOptionsExpanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.ContentCopy,
                                            contentDescription = stringResource(id = CoreR.string.copy_maze_seed)
                                        )
                                    }
                                )
                            }
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
                },
            )
        }
    ) { innerPadding ->
        if (!uiState.isMazeEmpty) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                MazePath(
                    modifier = Modifier.weight(1f),
                    items = uiState.maze.items,
                    mazeSeed = uiState.mazeSeed ?: 0,
                    onItemClick = onItemClick,
                    startScrollToCurrentItem = uiState.autoScrollToCurrentItem
                )

                if (uiState.isMazeCompleted) {
                    MazeCompletedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(
                                start = spaceMedium,
                                end = spaceMedium,
                                bottom = spaceMedium
                            ),
                        onRestartClick = { uiEvent(MazeScreenUiEvent.RestartMaze) }
                    )
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun MazeScreenPreview() {
    val completedItems = List(3) {
        MazeItem.Wordle(
            wordleWord = WordleWord("1+1=2"),
            difficulty = QuestionDifficulty.Easy,
            played = true,
            wordleQuizType = WordleQuizType.MATH_FORMULA,
            mazeSeed = 0
        )
    }

    val otherItems = List(8) {
        MazeItem.Wordle(
            wordleWord = WordleWord("1+1=2"),
            difficulty = QuestionDifficulty.Easy,
            wordleQuizType = WordleQuizType.MATH_FORMULA,
            mazeSeed = 0
        )
    }

    val mazeItems = (completedItems + otherItems).toPersistentList()

    NewQuizTheme {
        Surface {
            MazeScreenImpl(
                uiState = MazeScreenUiState(
                    loading = false,
                    maze = MazeQuiz(items = mazeItems)
                ),
                navigateBack = {},
                uiEvent = {},
                onItemClick = {},
            )
        }
    }
}
