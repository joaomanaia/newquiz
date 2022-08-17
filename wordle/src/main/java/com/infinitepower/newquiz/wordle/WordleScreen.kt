package com.infinitepower.newquiz.wordle

import androidx.annotation.Keep
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.ads.admob.BannerAd
import com.infinitepower.newquiz.core.ui.ads.admob.getAdaptiveAdSize
import com.infinitepower.newquiz.core.util.ads.admob.rewarded.RewardedAdUtil
import com.infinitepower.newquiz.core.util.ads.admob.rewarded.RewardedAdUtilImpl
import com.infinitepower.newquiz.core.util.compose.activity.getActivity
import com.infinitepower.newquiz.model.wordle.WordleChar
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleRowItem
import com.infinitepower.newquiz.wordle.components.WordleKeyBoard
import com.infinitepower.newquiz.wordle.components.WordleRowComponent
import com.infinitepower.newquiz.wordle.components.getItemRowBackgroundColor
import com.infinitepower.newquiz.wordle.components.getItemRowTextColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import com.infinitepower.newquiz.core.R as CoreR

private const val WORDLE_BANNER_AD_ID = "ca-app-pub-1923025671607389/6274577111"
private const val WORDLE_REWARDED_AD_ID = "ca-app-pub-1923025671607389/9652850233"

@Keep
data class WordleScreenNavArgs(
    val rowLimit: Int = Int.MAX_VALUE,
    val word: String? = null,
    val date: String? = null
)

@Composable
@Destination(navArgsDelegate = WordleScreenNavArgs::class)
fun WordleScreen(
    navigator: DestinationsNavigator,
    wordleScreenViewModel: WordleScreenViewModel = hiltViewModel()
) {
    val uiState by wordleScreenViewModel.uiState.collectAsState()

    val context = LocalContext.current

    val rewardedAdUtil: RewardedAdUtil? = remember(context) {
        val activity = context.getActivity() ?: return@remember null

        RewardedAdUtilImpl(activity)
    }

    WordleScreenImpl(
        uiState = uiState,
        onEvent = wordleScreenViewModel::onEvent,
        onBackClick = navigator::popBackStack,
        rewardedAdUtil = rewardedAdUtil
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun WordleScreenImpl(
    uiState: WordleScreenUiState,
    onEvent: (event: WordleScreenUiEvent) -> Unit,
    onBackClick: () -> Unit,
    rewardedAdUtil: RewardedAdUtil?
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val (gameOverPopupVisible, setGameOverPopupVisibility) = remember(uiState.isGameOver) {
        mutableStateOf(uiState.isGameOver)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val (infoDialogVisible, setInfoDialogVisibility) = remember {
        mutableStateOf(false)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = stringResource(id = CoreR.string.wordle))
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
        },
        bottomBar = {
            BannerAd(
                adId = WORDLE_BANNER_AD_ID,
                adSize = getAdaptiveAdSize()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spaceMedium),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = spaceMedium)
            ) {
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

                items(items = uiState.rows) { rowItem ->
                    WordleRowComponent(
                        wordleRowItem = rowItem,
                        word = uiState.word.orEmpty(),
                        onItemClick = { index ->
                            onEvent(WordleScreenUiEvent.OnRemoveKeyClick(index))
                        },
                        isColorBlindEnabled = uiState.isColorBlindEnabled,
                        isLetterHintsEnabled = uiState.isLetterHintEnabled,
                        modifier = Modifier.testTag(WordleScreenTestTags.WORDLE_ROW)
                    )
                }
            }

            if (!uiState.loading && !uiState.isGamedEnded) {
                WordleKeyBoard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small)
                        .padding(bottom = MaterialTheme.spacing.extraLarge)
                        .testTag(WordleScreenTestTags.KEYBOARD),
                    keys = WordleScreenUiState.ALL_LETTERS.toCharArray(),
                    keysDisabled = uiState.keysDisabled,
                    onKeyClick = { key ->
                        onEvent(WordleScreenUiEvent.OnKeyClick(key))
                    }
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
    }

    if (gameOverPopupVisible) {
        AlertDialog(
            onDismissRequest = { setGameOverPopupVisibility(false) },
            title = {
                Text(text = stringResource(id = CoreR.string.game_over))
            },
            text = {
                Text(text = stringResource(id = CoreR.string.you_lost_the_game_watch_ad_q))
            },
            confirmButton = {
                val snackBarMessage = stringResource(id = CoreR.string.loading_rewarded_ad)
                TextButton(
                    onClick = {
                        setGameOverPopupVisibility(false)
                        scope.launch {
                            snackbarHostState.showSnackbar(snackBarMessage)
                        }
                        rewardedAdUtil?.loadAndShow(
                            adId = WORDLE_REWARDED_AD_ID,
                            onUserEarnedReward = { onEvent(WordleScreenUiEvent.AddOneRow) }
                        )
                    }
                ) {
                    Text(text = stringResource(id = CoreR.string.watch_ad))
                }
            },
            dismissButton = {
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WordleRowComponent(
                    wordleRowItem = rowItem,
                    word = "QUIZ",
                    isPreview = true,
                    isColorBlindEnabled = isColorBlindEnabled,
                    onItemClick = {}
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                InfoDialogCard(isColorBlindEnabled = isColorBlindEnabled)
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
                    append(" is not in the target word.")
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
                    append(" is in the word but in the wrong spot.")
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
                    append(" is in the word and in the correct spot.")
                }
            )
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
@PreviewNightLight
private fun WordleScreenPreview() {
    val emptyRow = WordleRowItem(
        items = List(6) {
            WordleItem.Empty
        }
    )

    val rowItems = listOf(
        WordleRowItem(
            items = listOf(
                WordleItem.None(char = WordleChar('A')),
                WordleItem.None(char = WordleChar('A')),
                WordleItem.None(char = WordleChar('A')),
                WordleItem.None(char = WordleChar('A')),
                WordleItem.Present(char = WordleChar('B')),
                WordleItem.Correct(char = WordleChar('C'))
            )
        ),
        emptyRow,
        emptyRow,
        emptyRow,
        emptyRow,
        emptyRow
    )

    NewQuizTheme {
        WordleScreenImpl(
            uiState = WordleScreenUiState(
                rows = rowItems,
                currentRowPosition = 0,
                loading = false
            ),
            onEvent = {},
            onBackClick = {},
            rewardedAdUtil = null
        )
    }
}