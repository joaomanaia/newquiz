package com.infinitepower.newquiz.feature.maze.level_results

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.navigation.MazeNavigator
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.ui.SnackbarController
import com.infinitepower.newquiz.feature.maze.level_results.components.LevelCompletedContent
import com.infinitepower.newquiz.feature.maze.level_results.components.LevelFailedContent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(navArgsDelegate = LevelResultsScreenArgs::class)
internal fun LevelResultsScreen(
    navigator: DestinationsNavigator,
    mazeNavigator: MazeNavigator,
    viewModel: LevelResultsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LevelResultsScreenImpl(
        uiState = uiState,
        popBackStack = navigator::popBackStack,
        navigateToNextLevel = { uiState.nextAvailableItem?.let(mazeNavigator::navigateToGame) },
        retryLevel = { uiState.currentItem?.let(mazeNavigator::navigateToGame) }
    )
}

@Composable
private fun LevelResultsScreenImpl(
    popBackStack: () -> Unit,
    navigateToNextLevel: () -> Unit,
    retryLevel: () -> Unit,
    uiState: LevelResultsScreenUiState
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
    ) { innerPadding ->
        when {
            uiState.loading -> CircularProgressIndicator()
            !uiState.loading && uiState.error != null -> {
                val currentPopBackStack by rememberUpdatedState(popBackStack)

                LaunchedEffect(Unit) {
                    SnackbarController.sendShortMessage(uiState.error)
                    currentPopBackStack()
                }
            }

            else -> {
                if (uiState.completed) {
                    LevelCompletedContent(
                        modifier = Modifier.padding(innerPadding),
                        onNext = navigateToNextLevel,
                        popBackStack = popBackStack,
                        nextLevelExists = uiState.nextAvailableItem != null
                    )
                } else {
                    LevelFailedContent(
                        onRetry = retryLevel,
                        popBackStack = popBackStack,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class LevelResultsScreenArgs(
    val mazeItemId: Int
)

@Composable
@PreviewLightDark
private fun LevelResultsScreenPreview() {
    NewQuizTheme {
        LevelResultsScreenImpl(
            uiState = LevelResultsScreenUiState(),
            popBackStack = {},
            navigateToNextLevel = {},
            retryLevel = {}
        )
    }
}
