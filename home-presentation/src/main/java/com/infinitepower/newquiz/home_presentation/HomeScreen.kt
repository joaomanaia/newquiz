package com.infinitepower.newquiz.home_presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home_card.HomeListContent
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.home_presentation.data.getHomeCardItemData
import com.infinitepower.newquiz.home_presentation.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeScreenNavigator: HomeScreenNavigator,
    homeViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    val homeCardItemData by remember(uiState.recommendedHomeGame) {
        derivedStateOf {
            getHomeCardItemData(homeNavigator = homeScreenNavigator)
                .toMutableList()
                .apply {
                    if (!uiState.isLoggedIn && uiState.showLoginCard) {
                        val homeLoginCardData = getLoginHomeData(
                            navigateToLoginScreen = { navigator.navigate(LoginScreenDestination) },
                            dismissLoginCard = { homeViewModel.onEvent(HomeScreenUiEvent.DismissLoginCard) }
                        )
                        add(0, homeLoginCardData)
                    }

                    val hasRecommendedHomeGame = uiState.recommendedHomeGame != RecommendedHomeGame.NO_GAME

                    if (hasRecommendedHomeGame) {
                        addAll(getRecommendedHomeData(uiState.recommendedHomeGame, homeScreenNavigator))
                    }
                }.toList()
        }
    }

    HomeListContent(items = homeCardItemData)

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("HomeScreen")
    }
}

private fun getRecommendedHomeData(
    recommendedHomeGame: RecommendedHomeGame,
    homeScreenNavigator: HomeScreenNavigator
): List<HomeCardItem> {
    val navigateAction = {
        when (recommendedHomeGame) {
            RecommendedHomeGame.QUICK_MULTICHOICEQUIZ -> homeScreenNavigator.navigateToQuickQuiz()
            RecommendedHomeGame.WORDLE_INFINITE -> homeScreenNavigator.navigateToWordle()
            RecommendedHomeGame.FLAG -> homeScreenNavigator.navigateToFlagQuiz()
            RecommendedHomeGame.LOGO -> homeScreenNavigator.navigateToLogoQuiz()
            else -> throw IllegalArgumentException()
        }
    }

    return listOf(
        HomeCardItem.GroupTitle(
            title = CoreR.string.today_game,
        ),
        HomeCardItem.LargeCard(
            title = CoreR.string.today_random_game,
            icon = CardIcon.Icon(Icons.Rounded.QuestionMark),
            onClick = navigateAction,
            backgroundPrimary = true
        )
    )
}

private fun getLoginHomeData(
    navigateToLoginScreen: () -> Unit,
    dismissLoginCard: () -> Unit
): HomeCardItem = HomeCardItem.CustomItem {
    SignInCard(
        modifier = Modifier.fillMaxWidth(),
        onSignInClick = navigateToLoginScreen,
        onDismissClick = dismissLoginCard
    )
}

@Composable
private fun SignInCard(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(spaceMedium),
        ) {
            Text(
                text = stringResource(id = CoreR.string.save_you_progress_description),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(spaceMedium))
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spaceMedium)
            ) {
                TextButton(onClick = onDismissClick) {
                    Text(text = stringResource(id = CoreR.string.dismiss))
                }
                FilledTonalButton(onClick = onSignInClick) {
                    Text(text = stringResource(id = CoreR.string.sign_in))
                }
            }
        }
    }
}

@Composable
@PreviewNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun HomeScreenPreview() {
    val homeCardItemData = remember {
        getHomeCardItemData(homeNavigator = HomeNavigatorPreviewImpl())
    }

    NewQuizTheme {
        HomeListContent(items = homeCardItemData)
    }
}