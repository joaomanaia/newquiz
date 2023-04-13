package com.infinitepower.newquiz.home_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Compare
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizScreenDestination
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.components.HomeGroupTitle
import com.infinitepower.newquiz.core.ui.home_card.components.HomeLargeCard
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.home_presentation.components.SignInCard
import com.infinitepower.newquiz.home_presentation.destinations.LoginScreenDestination
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeNavigator: HomeScreenNavigator,
    homeViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenImpl(
        uiState = uiState,
        navigator = navigator,
        homeNavigator = homeNavigator,
        onEvent = homeViewModel::onEvent
    )
}

@Composable
@ExperimentalMaterial3Api
private fun HomeScreenImpl(
    uiState: HomeScreenUiState,
    navigator: DestinationsNavigator,
    homeNavigator: HomeScreenNavigator,
    onEvent: (event: HomeScreenUiEvent) -> Unit,
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val navigateToRandomComparisonGame = {
        val randomCategory = uiState.comparisonQuizCategories.random()
        val randomComparisonMode = ComparisonModeByFirst.values().random()

        navigator.navigate(
            ComparisonQuizScreenDestination(
                category = randomCategory,
                comparisonMode = randomComparisonMode
            )
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(spaceMedium),
        contentPadding = PaddingValues(
            start = spaceMedium,
            end = spaceMedium,
            bottom = MaterialTheme.spacing.large,
        )
    ) {
        if (uiState.showLoginCard) {
            item {
                SignInCard(
                    modifier = Modifier.fillMaxWidth(),
                    onSignInClick = { navigator.navigate(LoginScreenDestination) },
                    onDismissClick = { onEvent(HomeScreenUiEvent.DismissLoginCard) }
                )
            }
        }

        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.maze))
        }

        item {
            HomeLargeCard(
                modifier = Modifier.fillParentMaxWidth(),
                data = HomeCardItem.LargeCard(
                    title = CoreR.string.quiz_with_random_categories,
                    icon = CardIcon.Lottie(LottieCompositionSpec.RawRes(CoreR.raw.space)),
                    onClick = homeNavigator::navigateToMaze,
                    requireInternetConnection = false,
                    backgroundPrimary = true
                )
            )
        }

        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.multi_choice_quiz))
        }

        item {
            HomeLargeCard(
                modifier = Modifier.fillParentMaxWidth(),
                data = HomeCardItem.LargeCard(
                    title = CoreR.string.quiz_with_random_categories,
                    icon = CardIcon.Icon(Icons.Rounded.List),
                    onClick = homeNavigator::navigateToMultiChoiceQuiz,
                    requireInternetConnection = true
                )
            )
        }

        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.wordle))
        }

        item {
            HomeLargeCard(
                modifier = Modifier.fillParentMaxWidth(),
                data = HomeCardItem.LargeCard(
                    title = CoreR.string.quiz_with_random_categories,
                    icon = CardIcon.Icon(Icons.Rounded.Password),
                    onClick = homeNavigator::navigateToWordleQuiz,
                    requireInternetConnection = false
                )
            )
        }

        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.comparison_quiz))
        }

        item {
            HomeLargeCard(
                modifier = Modifier.fillParentMaxWidth(),
                data = HomeCardItem.LargeCard(
                    title = CoreR.string.quiz_with_random_categories,
                    icon = CardIcon.Icon(Icons.Rounded.Compare),
                    onClick = navigateToRandomComparisonGame,
                    requireInternetConnection = true
                )
            )
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeScreenPreview() {
    NewQuizTheme {
        Surface {
            HomeScreenImpl(
                uiState = HomeScreenUiState(
                    showLoginCard = true,
                    multiChoiceRecentCategories = emptyList()
                ),
                navigator = EmptyDestinationsNavigator,
                homeNavigator = EmptyHomeScreenNavigator,
                onEvent = {}
            )
        }
    }
}