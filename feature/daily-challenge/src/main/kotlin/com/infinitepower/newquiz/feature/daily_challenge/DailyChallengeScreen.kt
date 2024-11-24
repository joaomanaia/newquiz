package com.infinitepower.newquiz.feature.daily_challenge

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.icon.button.BackIconButton
import com.infinitepower.newquiz.core.util.asString
import com.infinitepower.newquiz.core.util.plus
import com.infinitepower.newquiz.feature.daily_challenge.components.DailyChallengeCard
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes
import com.infinitepower.newquiz.core.R as CoreR

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
        navigateToGame = dailyChallengeScreenNavigator::navigateWithGameEvent
    )
}

@Composable
@ExperimentalMaterial3Api
private fun DailyChallengeScreen(
    uiState: DailyChallengeScreenUiState,
    onBackClick: () -> Unit = {},
    onEvent: (event: DailyChallengeScreenUiEvent) -> Unit = {},
    navigateToGame: (
        event: GameEvent,
        comparisonQuizCategories: List<ComparisonQuizCategory>
    ) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val now by produceState(initialValue = Clock.System.now()) {
        while (true) {
            value = Clock.System.now()
            delay(timeMillis = 1000)
        }
    }

    val analyticsHelper = LocalAnalyticsHelper.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = CoreR.string.daily_challenge)) },
                navigationIcon = { BackIconButton(onClick = onBackClick) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding + PaddingValues(spaceMedium),
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
                    diamondsReward = task.diamondsReward,
                    userCanClaim = uiState.userAvailable,
                    onClaimClick = {
                        analyticsHelper.logEvent(
                            AnalyticsEvent.DailyChallengeItemClaim(
                                event = task.event,
                                steps = task.currentValue.toInt()
                            ),
                            AnalyticsEvent.EarnDiamonds(earned = task.diamondsReward.toInt())
                        )
                        onEvent(DailyChallengeScreenUiEvent.OnClaimTaskClick(task.event))
                    },
                    onCardClick = {
                        analyticsHelper.logEvent(
                            AnalyticsEvent.DailyChallengeItemClick(event = task.event)
                        )
                        navigateToGame(task.event, uiState.comparisonQuizCategories)
                    }
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun DailyChallengeScreenPreview() {
    val now = Clock.System.now()

    NewQuizTheme {
        Surface {
            DailyChallengeScreen(
                uiState = DailyChallengeScreenUiState(
                    tasks = List(10) {
                        DailyChallengeTask(
                            id = it,
                            title = UiText.DynamicString("Task $it"),
                            diamondsReward = 10u,
                            experienceReward = 100u,
                            isClaimed = false,
                            dateRange = now.minus(1.minutes)..now.plus(1.minutes),
                            currentValue = (0..10).random().toUInt(),
                            maxValue = 10u,
                            event = GameEvent.MultiChoice.EndQuiz
                        )
                    }
                ),
                onBackClick = {},
                onEvent = {},
                navigateToGame = { _, _ -> }
            )
        }
    }
}
