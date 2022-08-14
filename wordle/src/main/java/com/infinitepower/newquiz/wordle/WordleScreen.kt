package com.infinitepower.newquiz.wordle

import androidx.annotation.Keep
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
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
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Wordle")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.currentRowCompleted && !uiState.isGamedEnded) {
                ExtendedFloatingActionButton(
                    text = {
                        Text(text = "Verify")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "Verify"
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
                adId = "ca-app-pub-1923025671607389/6274577111",
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
                item {
                    if (uiState.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.testTag(
                                WordleScreenTestTags.LOADING_PROGRESS_INDICATOR
                            )
                        )
                    }
                }

                items(items = uiState.rows) { rowItem ->
                    WordleRowComponent(
                        wordleRowItem = rowItem,
                        onItemClick = { index ->
                            onEvent(WordleScreenUiEvent.OnRemoveKeyClick(index))
                        },
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
                            Text(text = "Play Again")
                        }
                    }

                    Button(
                        onClick = onBackClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Back")
                    }
                }
            }
        }
    }

    if (gameOverPopupVisible) {
        AlertDialog(
            onDismissRequest = { setGameOverPopupVisibility(false) },
            title = {
                Text(text = "Game Over")
            },
            text = {
                Text(text = "You lost the game.\nDo you want to watch one ad to try one more row?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        setGameOverPopupVisibility(false)
                        scope.launch {
                            snackbarHostState.showSnackbar("Loading rewarded ad!")
                        }
                        rewardedAdUtil?.loadAndShow(
                            adId = "ca-app-pub-1923025671607389/9652850233",
                            onUserEarnedReward = { onEvent(WordleScreenUiEvent.AddOneRow) }
                        )
                    }
                ) {
                    Text(text = "Watch ad")
                }
            },
            dismissButton = {
                TextButton(onClick = { setGameOverPopupVisibility(false) }) {
                    Text(text = "Close")
                }
            }
        )
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