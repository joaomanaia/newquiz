package com.infinitepower.newquiz.wordle

import androidx.annotation.Keep
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.infinitepower.newquiz.core.navigation.MazeNavigator
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.animationsEnabled
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.data.util.translation.getWordleTitle
import com.infinitepower.newquiz.model.wordle.WordleChar
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleRowItem
import com.infinitepower.newquiz.wordle.components.InfoDialog
import com.infinitepower.newquiz.wordle.components.WordleKeyBoard
import com.infinitepower.newquiz.wordle.components.WordleRowComponent
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import com.infinitepower.newquiz.core.R as CoreR

@Keep
data class WordleScreenNavArgs(
    val rowLimit: Int = Int.MAX_VALUE,
    val word: String? = null,
    val quizType: WordleQuizType = WordleQuizType.TEXT,
    val mazeItemId: String? = null,
    val textHelper: String? = null
)

@Composable
@Destination(
    navArgsDelegate = WordleScreenNavArgs::class,
    deepLinks = [
        DeepLink(uriPattern = "newquiz://wordleinfinite")
    ]
)
fun WordleScreen(
    navigator: DestinationsNavigator,
    mazeNavigator: MazeNavigator,
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    wordleScreenViewModel: WordleScreenViewModel = hiltViewModel()
) {
    val uiState by wordleScreenViewModel.uiState.collectAsStateWithLifecycle()

    val backStackEntry = navController.currentBackStackEntry
    val args = backStackEntry?.let { WordleScreenDestination.argsFrom(it) }
    val mazeItemId = args?.mazeItemId?.toIntOrNull()

    WordleScreenImpl(
        fromMaze = mazeItemId != null,
        uiState = uiState,
        onEvent = wordleScreenViewModel::onEvent,
        onBackClick = navigator::popBackStack,
        windowSizeClass = windowSizeClass
    )

    // If the game is over and is from maze, navigate to maze results
    LaunchedEffect(uiState.isGamedEnded) {
        if (uiState.isGamedEnded && mazeItemId != null) {
            if (!uiState.isGameOver) {
                // Show a delay before moving to maze results
                delay(NAV_TO_RESULTS_DELAY_MILLIS)
                mazeNavigator.navigateToMazeResults(mazeItemId)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
private fun WordleScreenImpl(
    fromMaze: Boolean = false,
    uiState: WordleScreenUiState,
    onEvent: (event: WordleScreenUiEvent) -> Unit,
    onBackClick: () -> Unit,
    windowSizeClass: WindowSizeClass,
    animationsEnabled: Boolean = MaterialTheme.animationsEnabled.wordle
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val (gameOverPopupVisible, setGameOverPopupVisibility) = remember(uiState.isGameOver && !fromMaze) {
        mutableStateOf(uiState.isGameOver)
    }

    val (infoDialogVisible, setInfoDialogVisibility) = remember {
        mutableStateOf(false)
    }

    val screenTitle = uiState.wordleQuizType.getWordleTitle()

    val rowLayout = remember(windowSizeClass) {
        windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
                && windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
    }

    val keyboardBottomPadding = if (!rowLayout) {
        MaterialTheme.spacing.extraLarge
    } else 0.dp

    val scrollState = rememberLazyListState()

    LaunchedEffect(key1 = uiState.currentRowPosition) {
        if (uiState.currentRowPosition > 0 && uiState.rows.isNotEmpty()) {
            scrollState.animateScrollToItem(uiState.rows.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle.asString()) },
                navigationIcon = { BackIconButton(onClick = onBackClick) },
                actions = {
                    IconButton(onClick = { setInfoDialogVisibility(true) }) {
                        Icon(
                            imageVector = Icons.Rounded.Info,
                            contentDescription = stringResource(id = CoreR.string.info)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.currentRowCompleted && !uiState.isGamedEnded) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(text = stringResource(id = CoreR.string.verify))
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = stringResource(id = CoreR.string.verify)
                        )
                    },
                    onClick = {
                        onEvent(WordleScreenUiEvent.VerifyRow)
                    },
                    modifier = Modifier.testTag(WordleScreenTestTags.VERIFY_FAB)
                )
            }
        }
    ) { innerPadding ->
        WordleContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            rowLayout = rowLayout,
            wordsScrollState = scrollState,
            wordleContent = {
                if (uiState.loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.testTag(
                                WordleScreenTestTags.LOADING_PROGRESS_INDICATOR
                            )
                        )
                    }
                }

                if (uiState.textHelper != null) {
                    item {
                        Text(
                            text = uiState.textHelper,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(horizontal = spaceMedium)
                        )
                    }
                }

                itemsIndexed(items = uiState.rows) { index, rowItem ->
                    val isCurrentRow = uiState.currentRowPosition == index

                    WordleRowComponent(
                        wordleRowItem = rowItem,
                        word = uiState.word.orEmpty(),
                        onItemClick = { itemIndex ->
                            onEvent(
                                WordleScreenUiEvent.OnRemoveKeyClick(
                                    itemIndex
                                )
                            )
                        },
                        isColorBlindEnabled = uiState.isColorBlindEnabled,
                        isLetterHintsEnabled = uiState.isLetterHintEnabled,
                        modifier = Modifier.testTag(WordleScreenTestTags.WORDLE_ROW),
                        enabled = isCurrentRow,
                        animationEnabled = animationsEnabled
                    )
                }
            },
            keyboardContent = {
                if (!uiState.loading && !uiState.isGamedEnded) {
                    WordleKeyBoard(
                        modifier = Modifier
                            // .padding(horizontal = MaterialTheme.spacing.small)
                            // .padding(bottom = keyboardBottomPadding)
                            .testTag(WordleScreenTestTags.KEYBOARD),
                        rowLayout = rowLayout,
                        keys = uiState.wordleKeys,
                        disabledKeys = uiState.keysDisabled,
                        onKeyClick = { key ->
                            onEvent(WordleScreenUiEvent.OnKeyClick(key))
                        },
                        wordleQuizType = uiState.wordleQuizType ?: WordleQuizType.TEXT,
                        windowWidthSizeClass = windowSizeClass.widthSizeClass,
                        contentPadding = PaddingValues(
                            start = MaterialTheme.spacing.small,
                            end = MaterialTheme.spacing.small,
                            bottom = keyboardBottomPadding
                        )
                    )
                }

                if (uiState.isGamedEnded && !fromMaze) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spaceMedium),
                        modifier = Modifier.padding(spaceMedium)
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(WordleScreenUiEvent.OnPlayAgainClick) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(id = CoreR.string.play_again))
                        }

                        Button(
                            onClick = onBackClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = stringResource(id = CoreR.string.back))
                        }
                    }
                }
            }
        )
    }

    if (gameOverPopupVisible) {
        AlertDialog(
            onDismissRequest = { setGameOverPopupVisibility(false) },
            title = {
                Text(text = stringResource(id = CoreR.string.game_over))
            },
            confirmButton = {
                TextButton(onClick = { setGameOverPopupVisibility(false) }) {
                    Text(text = stringResource(id = CoreR.string.close))
                }
            }
        )
    }

    if (infoDialogVisible) {
        InfoDialog(
            isColorBlindEnabled = uiState.isColorBlindEnabled,
            onDismissRequest = { setInfoDialogVisibility(false) }
        )
    }
}

@Composable
private fun WordleContainer(
    modifier: Modifier = Modifier,
    rowLayout: Boolean = false,
    wordsScrollState: LazyListState = rememberLazyListState(),
    wordleContent: LazyListScope.() -> Unit,
    keyboardContent: @Composable BoxScope.() -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    if (rowLayout) {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = spaceMedium),
                content = wordleContent,
                state = wordsScrollState
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                keyboardContent()
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = spaceMedium),
                content = wordleContent,
                state = wordsScrollState
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                keyboardContent()
            }
        }
    }
}

private const val NAV_TO_RESULTS_DELAY_MILLIS = 1000L

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal object WordleScreenTestTags {
    const val VERIFY_FAB = "VERIFY_FAB"
    const val LOADING_PROGRESS_INDICATOR = "LOADING_PROGRESS_INDICATOR"
    const val KEYBOARD = "KEYBOARD"
    const val WORDLE_ROW = "WORDLE_ROW"
}

@Composable
@PreviewScreenSizes
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun WordleScreenPreview() {
    val rowItems = listOf(
        WordleRowItem(
            items = listOf(
                WordleItem.Correct(char = WordleChar('Q')),
                WordleItem.None(char = WordleChar('A')),
                WordleItem.Present(char = WordleChar('Z')),
                WordleItem.Present(char = WordleChar('I')),
                WordleItem.Empty
            )
        )
    )

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        WordleScreenImpl(
            uiState = WordleScreenUiState(
                word = "QUIZZ",
                rows = rowItems,
                currentRowPosition = 0,
                loading = false,
                wordleQuizType = WordleQuizType.TEXT,
                textHelper = "Wordle text helper for word.",
            ),
            onEvent = {},
            onBackClick = {},
            windowSizeClass = windowSizeClass,
            animationsEnabled = false
        )
    }
}
