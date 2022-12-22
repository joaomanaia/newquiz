package com.infinitepower.newquiz.math_quiz.maze

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.math_quiz.maze.components.GenerateMazeComponent
import com.infinitepower.newquiz.math_quiz.maze.components.MazeComponent
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.math_quiz.maze.MathQuizMaze
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
fun MathMazeScreen(
    navigator: DestinationsNavigator,
    viewModel: MathMazeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MathMazeScreenImpl(
        uiState = uiState,
        navigateBack = navigator::popBackStack,
        uiEvent = viewModel::onEvent,
        onItemClick = { item ->
            navigator.navigate(
                WordleScreenDestination(
                    word = item.formula.fullFormulaWithoutSpaces,
                    quizType = WordleQuizType.MATH_FORMULA,
                    mazeItemId = item.id
                )
            )
        }
    )
}

@Composable
@ExperimentalMaterial3Api
private fun MathMazeScreenImpl(
    uiState: MathMazeScreenUiState,
    navigateBack: () -> Unit,
    uiEvent: (event: MathMazeScreenUiEvent) -> Unit,
    onItemClick: (item: MathQuizMaze.MazeItem) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val formulas = uiState.mathMaze.formulas

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.math_maze))
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
                        IconButton(onClick = { uiEvent(MathMazeScreenUiEvent.RestartMaze) }) {
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
                    onGenerateClick = { seed ->
                        uiEvent(MathMazeScreenUiEvent.GenerateMaze(seed))
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
fun MathMazeScreenPreview() {
    val completedItems = List(9) {
        MathQuizMaze.MazeItem(
            id = it,
            formula = MathFormula.fromStringFullFormula("1+1=2"),
            difficulty = QuestionDifficulty.Easy,
            played = true
        )
    }

    val otherItems = List(20) {
        MathQuizMaze.MazeItem(
            id = it,
            formula = MathFormula.fromStringFullFormula("1+1=2"),
            difficulty = QuestionDifficulty.Easy
        )
    }

    val mazeItems = completedItems + otherItems

    NewQuizTheme {
        MathMazeScreenImpl(
            uiState = MathMazeScreenUiState(
                loading = false,
                mathMaze = MathQuizMaze(formulas = mazeItems)
            ),
            navigateBack = {},
            uiEvent = {},
            onItemClick = {}
        )
    }
}