package com.infinitepower.newquiz.home_presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizScreenDestination
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.PreviewNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.ui.components.rememberIsInternetAvailable
import com.infinitepower.newquiz.core.ui.home_card.components.HomeGroupTitle
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.home_presentation.components.SignInCard
import com.infinitepower.newquiz.home_presentation.destinations.LoginScreenDestination
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.multi_choice_quiz.toBaseCategory
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.list.util.MultiChoiceQuizListUtils
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.infinitepower.newquiz.wordle.list.getRandomWordleCategory
import com.infinitepower.newquiz.wordle.list.getWordleCategories
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun HomeScreen(
    navigator: DestinationsNavigator,
    homeViewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenImpl(
        uiState = uiState,
        navigator = navigator,
        onEvent = homeViewModel::onEvent
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("HomeScreen")
    }
}

@Composable
@ExperimentalMaterial3Api
private fun HomeScreenImpl(
    uiState: HomeScreenUiState,
    navigator: DestinationsNavigator,
    onEvent: (event: HomeScreenUiEvent) -> Unit,
) {
    val spaceMedium = MaterialTheme.spacing.medium

    val isInternetAvailable = rememberIsInternetAvailable()

    val multiChoiceCategories = remember {
        MultiChoiceQuizListUtils.getRecentCategories(
            recentCategories = uiState.multiChoiceRecentCategories,
            allCategories = multiChoiceQuestionCategories,
            isInternetAvailable = isInternetAvailable
        )
    }

    val wordleCategory = remember {
        getRandomWordleCategory(
            allCategories = getWordleCategories(),
            isInternetAvailable = isInternetAvailable
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
            HomeGroupTitle(title = stringResource(id = CoreR.string.multi_choice_quiz))
        }

        items(
            items = multiChoiceCategories,
            key = { category -> "multichoice_category_${category.id}" }
        ) { category ->
            CategoryComponent(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(120.dp),
                title = category.name.asString(),
                imageUrl = category.image,
                onClick = {
                    navigator.navigate(MultiChoiceQuizScreenDestination(category = category.toBaseCategory()))
                },
                enabled = isInternetAvailable || !category.requireInternetConnection
            )
        }

        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.wordle))
        }

        item {
            CategoryComponent(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(120.dp),
                title = wordleCategory.name.asString(),
                imageUrl = wordleCategory.image,
                onClick = {
                    navigator.navigate(WordleScreenDestination(quizType = wordleCategory.wordleQuizType))
                },
                enabled = isInternetAvailable || !wordleCategory.requireInternetConnection
            )
        }

        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.comparison_quiz))
        }

        items(
            items = uiState.comparisonQuizCategories,
            key = { category -> category.id }
        ) { category ->
            CategoryComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                title = category.title,
                imageUrl = category.imageUrl,
                onClick = {
                    navigator.navigate(
                        ComparisonQuizScreenDestination(
                            category = category,
                            comparisonMode = ComparisonModeByFirst.GREATER
                        )
                    )
                },
                enabled = isInternetAvailable
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
                onEvent = {}
            )
        }
    }
}