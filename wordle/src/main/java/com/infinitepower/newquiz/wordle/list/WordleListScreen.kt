package com.infinitepower.newquiz.wordle.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.ui.home.HomeLazyColumn
import com.infinitepower.newquiz.core.ui.home.homeCategoriesItems
import com.infinitepower.newquiz.core.ui.home_card.components.PlayRandomQuizCard
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun WordleListScreen(
    navigator: DestinationsNavigator,
    homeViewModel: WordleListScreenViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

    WordleListScreenImpl(
        uiState = uiState,
        navigateToWordleQuiz = { wordleQuizType ->
            navigator.navigate(WordleScreenDestination(quizType = wordleQuizType))
        }
    )
}

@Composable
@ExperimentalMaterial3Api
private fun WordleListScreenImpl(
    uiState: WordleListUiState,
    navigateToWordleQuiz: (wordleQuizType: WordleQuizType) -> Unit
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    var seeAllCategories by remember { mutableStateOf(false) }

    HomeLazyColumn {
        item {
            PlayRandomQuizCard(
                modifier = Modifier.fillParentMaxWidth(),
                title = stringResource(id = CoreR.string.quiz_with_random_categories),
                buttonTitle = stringResource(id = CoreR.string.random_quiz),
                containerMainColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    val randomCategory = WordleCategories.random(uiState.internetConnectionAvailable)
                    navigateToWordleQuiz(randomCategory.wordleQuizType)
                },
                enabled = uiState.internetConnectionAvailable,
            )
        }

        item {
            Text(
                text = stringResource(id = CoreR.string.categories),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        homeCategoriesItems(
            seeAllCategories = seeAllCategories,
            recentCategories = uiState.homeCategories.recentCategories,
            otherCategories = uiState.homeCategories.otherCategories,
            isInternetAvailable = uiState.internetConnectionAvailable,
            onCategoryClick = { category ->
                analyticsHelper.logEvent(
                    AnalyticsEvent.CategoryClicked(
                        game = AnalyticsEvent.Game.WORDLE,
                        categoryId = category.id
                    )
                )

                navigateToWordleQuiz(category.wordleQuizType)
            },
            onSeeAllCategoriesClick = { seeAllCategories = !seeAllCategories },
            showConnectionInfo = uiState.showCategoryConnectionInfo
        )
    }
}

@Composable
@PreviewLightDark
@OptIn(ExperimentalMaterial3Api::class)
private fun WordleListScreenPreview() {
    NewQuizTheme {
        Surface {
            WordleListScreenImpl(
                uiState = WordleListUiState(),
                navigateToWordleQuiz = {}
            )
        }
    }
}