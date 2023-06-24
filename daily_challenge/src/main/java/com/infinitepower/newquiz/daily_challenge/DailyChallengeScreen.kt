package com.infinitepower.newquiz.daily_challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.daily_challenge.components.DailyChallengeCard
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun DailyChallengeScreen(
    destinationsNavigator: DestinationsNavigator,
    dailyChallengeScreenNavigator: DailyChallengeScreenNavigator,
    viewModel: DailyChallengeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DailyChallengeScreen(
        uiState = uiState,
        onBackClick = destinationsNavigator::popBackStack,
        onEvent = viewModel::onEvent,
        dailyChallengeScreenNavigator = dailyChallengeScreenNavigator
    )
}

@Composable
@ExperimentalMaterial3Api
private fun DailyChallengeScreen(
    uiState: DailyChallengeScreenUiState,
    onBackClick: () -> Unit = {},
    onEvent: (event: DailyChallengeScreenUiEvent) -> Unit = {},
    dailyChallengeScreenNavigator: DailyChallengeScreenNavigator
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val now by produceState(initialValue = Clock.System.now()) {
        while (true) {
            value = Clock.System.now()
            delay(1000)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Daily Challenge") },
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(spaceMedium),
            verticalArrangement = Arrangement.spacedBy(spaceMedium)
        ) {
            items(
                items = uiState.tasks,
                key = { task -> task.id }
            ) { task ->
                DailyChallengeCard(
                    now = now,
                    title = task.title.asString(),
                    currentValue = task.currentValue,
                    maxValue = task.maxValue,
                    dateRange = task.dateRange,
                    isCompleted = task.isCompleted(),
                    isClaimed = task.isClaimed,
                    modifier = Modifier.fillParentMaxWidth(),
                    onClaimClick = { onEvent(DailyChallengeScreenUiEvent.OnClaimTaskClick(task.event)) },
                    onCardClick = {
                        dailyChallengeScreenNavigator.navigateWithGameEvent(task.event, uiState.comparisonQuizCategories)
                    }
                )
            }
        }
    }
}
