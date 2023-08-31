package com.infinitepower.newquiz.comparison_quiz.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizScreenDestination
import com.infinitepower.newquiz.comparison_quiz.list.components.ComparisonModeComponents
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home.HomeLazyColumn
import com.infinitepower.newquiz.core.ui.home.homeCategoriesItems
import com.infinitepower.newquiz.domain.repository.home.HomeCategories
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.model.toUiText
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
fun ComparisonQuizListScreen(
    navigator: DestinationsNavigator,
    viewModel: ComparisonQuizListScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val analyticsHelper = LocalAnalyticsHelper.current

    ComparisonQuizListScreenImpl(
        uiState = uiState,
        onCategoryClick = { category ->
            analyticsHelper.logEvent(
                AnalyticsEvent.CategoryClicked(
                    game = AnalyticsEvent.Game.COMPARISON_QUIZ,
                    categoryId = category.id,
                    otherData = mapOf(
                        "comparison_mode" to uiState.selectedMode.name.lowercase(),
                    )
                )
            )

            navigator.navigate(
                ComparisonQuizScreenDestination(
                    category = category.toEntity(),
                    comparisonMode = uiState.selectedMode
                )
            )
        },
        onSelectMode = { mode ->
            viewModel.onEvent(ComparisonQuizListScreenUiEvent.SelectMode(mode))
        }
    )
}

@Composable
private fun ComparisonQuizListScreenImpl(
    uiState: ComparisonQuizListScreenUiState,
    onCategoryClick: (category: ComparisonQuizCategory) -> Unit,
    onSelectMode: (mode: ComparisonMode) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium

    var seeAllCategories by remember { mutableStateOf(false) }

    HomeLazyColumn {
        item {
            Column {
                Text(
                    text = stringResource(id = CoreR.string.select_a_comparison_mode_for_the_first_item),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(spaceMedium))
                ComparisonModeComponents(
                    modifier = Modifier.fillParentMaxWidth(),
                    onModeClick = onSelectMode,
                    selectedMode = uiState.selectedMode
                )
            }
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
            onCategoryClick = onCategoryClick,
            onSeeAllCategoriesClick = { seeAllCategories = !seeAllCategories }
        )
    }
}

@Composable
@AllPreviewsNightLight
private fun ComparisonQuizListScreenPreview() {
    NewQuizTheme {
        Surface {
            ComparisonQuizListScreenImpl(
                uiState = ComparisonQuizListScreenUiState(
                    homeCategories = HomeCategories(
                        recentCategories = listOf(
                            ComparisonQuizCategory(
                                id = "test",
                                description = "Description",
                                name = "Title".toUiText(),
                                image = "",
                                questionDescription = ComparisonQuizCategory.QuestionDescription(
                                    greater = "Greater",
                                    less = "Less"
                                ),
                                formatType = ComparisonQuizFormatType.Number
                            )
                        ),
                        otherCategories = emptyList()
                    )
                ),
                onCategoryClick = {},
                onSelectMode = {}
            )
        }
    }
}
