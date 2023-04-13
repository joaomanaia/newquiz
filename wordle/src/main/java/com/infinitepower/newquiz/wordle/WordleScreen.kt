package com.infinitepower.newquiz.wordle

import androidx.annotation.Keep
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.model.wordle.WordleChar
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleRowItem
import com.infinitepower.newquiz.wordle.components.WordleKeyBoard
import com.infinitepower.newquiz.wordle.components.WordleRowComponent
import com.infinitepower.newquiz.wordle.components.getItemRowBackgroundColor
import com.infinitepower.newquiz.wordle.components.getItemRowTextColor
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Keep
data class WordleScreenNavArgs(
    val rowLimit: Int = Int.MAX_VALUE,
    val word: String? = null,
    val date: String? = null,
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
    windowSizeClass: WindowSizeClass,
    wordleScreenViewModel: WordleScreenViewModel = hiltViewModel()
) {
    val uiState by wordleScreenViewModel.uiState.collectAsStateWithLifecycle()

    WordleScreenImpl(
        uiState = uiState,
        onEvent = wordleScreenViewModel::onEvent,
        onBackClick = navigator::popBackStack,
        windowSizeClass = windowSizeClass
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun WordleScreenImpl(
    uiState: WordleScreenUiState,
    onEvent: (event: WordleScreenUiEvent) -> Unit,
    onBackClick: () -> Unit,
    windowSizeClass: WindowSizeClass,
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val (gameOverPopupVisible, setGameOverPopupVisibility) = remember(uiState.isGameOver) {
        mutableStateOf(uiState.isGameOver)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val (infoDialogVisible, setInfoDialogVisibility) = remember {
        mutableStateOf(false)
    }

    val screenTitle = when (uiState.wordleQuizType) {
        WordleQuizType.NUMBER -> stringResource(id = CoreR.string.guess_the_number)
        WordleQuizType.MATH_FORMULA -> stringResource(id = CoreR.string.guess_math_formula)
        else -> stringResource(id = CoreR.string.wordle)
    }

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = screenTitle)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = CoreR.string.back)
                        )
                    }
                },
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

                if (uiState.errorMessage != null) {
                    item {
                        Text(
                            text = uiState.errorMessage,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
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
                        onItemClick = { itemIndex -> onEvent(WordleScreenUiEvent.OnRemoveKeyClick(itemIndex)) },
                        isColorBlindEnabled = uiState.isColorBlindEnabled,
                        isLetterHintsEnabled = uiState.isLetterHintEnabled,
                        modifier = Modifier.testTag(WordleScreenTestTags.WORDLE_ROW),
                        isPreview = !isCurrentRow,
                        animationEnabled = uiState.animationsEnabled
                    )
                }
            },
            keyboardContent = {
                if (!uiState.loading && !uiState.isGamedEnded) {
                    WordleKeyBoard(
                        modifier = Modifier
                            .padding(horizontal = MaterialTheme.spacing.small)
                            .padding(bottom = keyboardBottomPadding)
                            .testTag(WordleScreenTestTags.KEYBOARD),
                        rowLayout = rowLayout,
                        keys = uiState.wordleKeys,
                        disabledKeys = uiState.keysDisabled,
                        onKeyClick = { key ->
                            onEvent(WordleScreenUiEvent.OnKeyClick(key))
                        },
                        wordleQuizType = uiState.wordleQuizType ?: WordleQuizType.TEXT,
                        windowWidthSizeClass = windowSizeClass.widthSizeClass
                    )
                }

                if (uiState.isGamedEnded) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spaceMedium),
                        modifier = Modifier.padding(spaceMedium)
                    ) {
                        if (uiState.day == null) {
                            OutlinedButton(
                                onClick = { onEvent(WordleScreenUiEvent.OnPlayAgainClick) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = stringResource(id = CoreR.string.play_again))
                            }
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
private fun InfoDialog(
    isColorBlindEnabled: Boolean,
    onDismissRequest: () -> Unit
) {
    // Word: QUIZ
    val rowItem = WordleRowItem(
        items = listOf(
            WordleItem.fromChar('Q'), // None
            WordleItem.Present(WordleChar('U')),
            WordleItem.Correct(WordleChar('I')),
            WordleItem.Present(WordleChar('Z')),
        )
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(id = CoreR.string.info))
        },
        text = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    WordleRowComponent(
                        wordleRowItem = rowItem,
                        word = "QUIZ",
                        isPreview = true,
                        isColorBlindEnabled = isColorBlindEnabled,
                        onItemClick = {}
                    )
                }

                item { Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium)) }
               
                item {
                    InfoDialogCard(isColorBlindEnabled = isColorBlindEnabled)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = CoreR.string.close))
            }
        }
    )
}

@Composable
private fun InfoDialogCard(
    isColorBlindEnabled: Boolean
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val presentBackgroundColor = getItemRowBackgroundColor(
        item = WordleItem.Present(WordleChar('C')),
        isColorBlindEnabled = isColorBlindEnabled
    )
    val presentTextColor = getItemRowTextColor(
        item = WordleItem.Present(WordleChar('C')),
        isColorBlindEnabled = isColorBlindEnabled
    )

    val correctBackgroundColor = getItemRowBackgroundColor(
        item = WordleItem.Correct(WordleChar('C')),
        isColorBlindEnabled = isColorBlindEnabled
    )
    val correctTextColor = getItemRowTextColor(
        item = WordleItem.Correct(WordleChar('C')),
        isColorBlindEnabled = isColorBlindEnabled
    )

    Card {
        Column(
            modifier = Modifier.padding(spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            // Char none: Q
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    ) {
                        append('Q')
                    }
                    append(stringResource(id = CoreR.string.is_not_in_the_target_word_wordle))
                }
            )

            // Chars present: U, Z
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            background = presentBackgroundColor,
                            color = presentTextColor,
                            fontSize = 18.sp
                        )
                    ) {
                        append('U')
                    }
                    append(',')
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            background = presentBackgroundColor,
                            color = presentTextColor,
                            fontSize = 18.sp
                        )
                    ) {
                        append('Z')
                    }
                    append(stringResource(id = CoreR.string.is_in_the_word_but_in_the_wrong_spot_wordle))
                }
            )

            // Char correct: I
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            background = correctBackgroundColor,
                            color = correctTextColor,
                            fontSize = 18.sp
                        )
                    ) {
                        append('I')
                    }
                    append(stringResource(id = CoreR.string.is_in_the_word_and_in_the_correct_spot_wordle))
                }
            )
        }
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

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal object WordleScreenTestTags {
    const val VERIFY_FAB = "VERIFY_FAB"
    const val LOADING_PROGRESS_INDICATOR = "LOADING_PROGRESS_INDICATOR"
    const val KEYBOARD = "KEYBOARD"
    const val WORDLE_ROW = "WORDLE_ROW"
}

@Composable
@AllPreviewsNightLight
@Preview(
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,dpi=480,orientation=portrait",
    group = "Expanded"
)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
private fun WordleScreenPreview() {
    val rowItems = listOf(
        WordleRowItem(
            items = listOf(
                WordleItem.None(char = WordleChar('A')),
                WordleItem.None(char = WordleChar('A')),
                WordleItem.None(char = WordleChar('A')),
                WordleItem.None(char = WordleChar('A')),
                WordleItem.Present(char = WordleChar('B')),
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
                rows = rowItems,
                currentRowPosition = 0,
                loading = false,
                wordleQuizType = WordleQuizType.TEXT,
                textHelper = "Wordle text helper for word."
            ),
            onEvent = {},
            onBackClick = {},
            windowSizeClass = windowSizeClass
        )
    }
}
