package com.infinitepower.newquiz.wordle.daily_word

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.infinitepower.newquiz.core.common.viewmodel.NavEvent
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.wordle.daily_word.components.WordleCalendarView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
fun DailyWordSelectorScreen(
    navigator: DestinationsNavigator,
    dailyWordleSelectorViewModel: DailyWordleSelectorViewModel = hiltViewModel()
) {
    val uiState by dailyWordleSelectorViewModel.uiState.collectAsState()

    DailyWordSelectorScreenImpl(
        uiState = uiState,
        navEvent = dailyWordleSelectorViewModel.navEvent,
        onBackClick = navigator::popBackStack,
        navigate = navigator::navigate,
        onEvent = dailyWordleSelectorViewModel::onEvent
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DailyWordSelectorScreenImpl(
    uiState: DailyWordleSelectorUiState,
    navEvent: SharedFlow<NavEvent>,
    onBackClick: () -> Unit,
    navigate: (direction: Direction) -> Unit,
    onEvent: (event: DailyWordleSelectorUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val fourLettersStr = stringResource(id = CoreR.string.four_letters)
    val fiveLettersStr = stringResource(id = CoreR.string.five_letters)
    val sixLettersStr = stringResource(id = CoreR.string.six_letters)

    val tabs = remember {
        listOf(fourLettersStr, fiveLettersStr, sixLettersStr)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        navEvent
            .collect { event ->
                when (event) {
                    is NavEvent.PopBackStack -> onBackClick()
                    is NavEvent.Navigate -> navigate(event.direction)
                    is NavEvent.ShowSnackBar -> {
                        scope.launch {
                            snackbarHostState.showSnackbar(event.message)
                        }
                    }
                }
            }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
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
                    scrollBehavior = scrollBehavior
                )
                TabRow(
                    selectedTabIndex = uiState.pageIndex
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = uiState.pageIndex == index,
                            onClick = {
                                onEvent(DailyWordleSelectorUiEvent.OnChangePageIndex(index))
                            },
                            text = {
                                Text(text = tab)
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        WordleCalendarView(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = MaterialTheme.spacing.large),
            calendarItems = uiState.calendarItems,
            instant = uiState.instant,
            onInstantChanged = { instant ->
                onEvent(DailyWordleSelectorUiEvent.OnChangeInstant(instant))
            },
            onDateClick = { date ->
                onEvent(DailyWordleSelectorUiEvent.OnDateClick(date))
            }
        )
    }
}