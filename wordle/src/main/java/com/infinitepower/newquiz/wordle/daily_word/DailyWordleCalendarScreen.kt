package com.infinitepower.newquiz.wordle.daily_word

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.calendar.CalendarMonthViewImpl
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.common.viewmodel.NavEvent
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.wordle.daily_word.components.WordSizeSelectorCard
import com.infinitepower.newquiz.wordle.daily_word.components.WordleCalendarView
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun DailyWordleCalendarScreen(
    navigator: DestinationsNavigator,
    windowSizeClass: WindowSizeClass,
    dailyWordleCalendarViewModel: DailyWordleCalendarViewModel = hiltViewModel()
) {
    val uiState by dailyWordleCalendarViewModel.uiState.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }

    DailyWordleCalendarScreenImpl(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onBackClick = navigator::popBackStack,
        onEvent = dailyWordleCalendarViewModel::onEvent,
        windowSizeClass = windowSizeClass
    )

    LaunchedEffect(key1 = true) {
        dailyWordleCalendarViewModel
            .navEvent
            .collect { event ->
                when (event) {
                    is NavEvent.PopBackStack -> navigator.popBackStack()
                    is NavEvent.Navigate -> navigator.navigate(event.direction)
                    is NavEvent.ShowSnackBar -> snackBarHostState.showSnackbar(event.message)
                }
            }
    }
}

@Composable
@ExperimentalMaterial3Api
private fun DailyWordleCalendarScreenImpl(
    uiState: DailyWordleCalendarUiState,
    windowSizeClass: WindowSizeClass,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onBackClick: () -> Unit,
    onEvent: (event: DailyWordleCalendarUiEvent) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
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
                }
            )
        }
    ) { innerPadding ->
        DailyWordleCalendarContainer(
            modifier = Modifier.padding(innerPadding),
            windowSizeClass = windowSizeClass,
            wordSizeContent = {
                Surface(
                    tonalElevation = 8.dp,
                    modifier = Modifier.padding(spaceMedium),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(spaceMedium)
                    ) {
                        Text(
                            text = "Word Size",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        WordSizeSelectorCard(
                            sizes = listOf(4, 5, 6),
                            indexSizeSelected = uiState.pageIndex,
                            onItemClick = { index -> onEvent(DailyWordleCalendarUiEvent.OnChangePageIndex(index))}
                        )
                    }
                }
            },
            calendarContent = {
                WordleCalendarView(
                    days = uiState.calendarDays,
                    firstDayDate = uiState.firstDayDate,
                    savedCalendarItems = uiState.calendarSavedItems,
                    onDateClick = { date ->
                        onEvent(DailyWordleCalendarUiEvent.OnDateClick(date))
                    },
                    onNextMonthClick = { onEvent(DailyWordleCalendarUiEvent.NextMonth) },
                    onPreviousMonthClick = { onEvent(DailyWordleCalendarUiEvent.PreviousMonth) }
                )
            }
        )
    }
}

@Composable
private fun DailyWordleCalendarContainer(
    modifier: Modifier = Modifier,
    windowSizeClass: WindowSizeClass,
    wordSizeContent: @Composable BoxScope.() -> Unit,
    calendarContent: @Composable BoxScope.() -> Unit
) {
    if (windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
        && windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                content = wordSizeContent
            )
            Box(
                modifier = Modifier.weight(1f),
                content = calendarContent
            )
        }
    } else {
        val contentMaxWidth = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Medium -> 0.7f
            WindowWidthSizeClass.Expanded -> 0.5f
            else -> 1f
        }

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(contentMaxWidth),
                content = wordSizeContent
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(contentMaxWidth),
                content = calendarContent,
                contentAlignment = Alignment.Center
            )
        }
    }
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
private fun DailyWordleSelectorPreview() {
    val monthView = CalendarMonthViewImpl()

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(screenWidth, screenHeight))

    NewQuizTheme {
        Surface {
            DailyWordleCalendarScreenImpl(
                uiState = DailyWordleCalendarUiState(calendarDays = monthView.generateCalendarDays()),
                onBackClick = {},
                onEvent = {},
                windowSizeClass = windowSizeClass
            )
        }
    }
}