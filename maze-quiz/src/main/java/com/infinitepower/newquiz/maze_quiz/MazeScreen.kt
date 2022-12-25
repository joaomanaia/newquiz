package com.infinitepower.newquiz.maze_quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.maze_quiz.components.GenerateMazeComponent
import com.infinitepower.newquiz.maze_quiz.components.MazeComponent
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.maze.MazeQuiz.MazeItem
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.ramcosta.composedestinations.annotation.DeepLink
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(
    deepLinks = [
        DeepLink(uriPattern = "newquiz://maze")
    ]
)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
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

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("MazeScreen")
    }
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

    val formulas = uiState.mathMaze.items

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
                    if (!uiState.isMazeEmpty) {
                        IconButton(onClick = { uiEvent(MazeScreenUiEvent.RestartMaze) }) {
                            Icon(
                                imageVector = Icons.Rounded.RestartAlt,
                                contentDescription = "Restart"
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
                    onGenerateClick = { seed, gamesModeSelected ->
                        uiEvent(MazeScreenUiEvent.GenerateMaze(seed, gamesModeSelected))
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
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
fun MazeScreenPreview() {
    val completedItems = List(9) {
        MazeItem.Wordle(
            word = "1+1=2",
            difficulty = QuestionDifficulty.Easy,
            played = true,
            wordleQuizType = WordleQuizType.MATH_FORMULA
        )
    }

    val otherItems = List(20) {
        MazeItem.Wordle(
            word = "1+1=2",
            difficulty = QuestionDifficulty.Easy,
            wordleQuizType = WordleQuizType.MATH_FORMULA
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