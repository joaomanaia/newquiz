package com.infinitepower.newquiz.comparison_quiz.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizScreenDestination
import com.infinitepower.newquiz.comparison_quiz.list.components.ComparisonCategoryComponent
import com.infinitepower.newquiz.comparison_quiz.list.components.ComparisonModeComponent
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.rememberIsInternetAvailable
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.core.R as CoreR
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
fun ComparisonQuizListScreen(
    navigator: DestinationsNavigator,
    viewModel: ComparisonQuizListScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ComparisonQuizListScreenImpl(
        uiState = uiState,
        onCategoryClick = { category ->
            navigator.navigate(
                ComparisonQuizScreenDestination(
                    category = category,
                    comparisonMode = uiState.selectedMode
                )
            )
        },
        onSelectMode = { mode ->
            viewModel.onEvent(ComparisonQuizListScreenUiEvent.SelectMode(mode))
        }
    )

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("ComparisonQuizListScreen")
    }
}

@Composable
private fun ComparisonQuizListScreenImpl(
    uiState: ComparisonQuizListScreenUiState,
    onCategoryClick: (category: ComparisonQuizCategory) -> Unit,
    onSelectMode: (mode: ComparisonModeByFirst) -> Unit
) {
    val spaceMedium = MaterialTheme.spacing.medium
    
    val isInternetAvailable = rememberIsInternetAvailable()

    LazyColumn(
        contentPadding = PaddingValues(spaceMedium),
        verticalArrangement = Arrangement.spacedBy(spaceMedium)
    ) {
        item {
            Column {
                Text(
                    text = stringResource(id = CoreR.string.select_a_comparison_mode_for_the_first_item),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(spaceMedium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(spaceMedium)
                ) {
                    ComparisonModeComponent(
                        mode = ComparisonModeByFirst.GREATER,
                        modifier = Modifier.weight(1f),
                        selected = uiState.selectedMode == ComparisonModeByFirst.GREATER,
                        onClick = { onSelectMode(ComparisonModeByFirst.GREATER) }
                    )
                    ComparisonModeComponent(
                        mode = ComparisonModeByFirst.LESSER,
                        modifier = Modifier.weight(1f),
                        selected = uiState.selectedMode == ComparisonModeByFirst.LESSER,
                        onClick = { onSelectMode(ComparisonModeByFirst.LESSER) }
                    )
                }
            }
        }

        item {
            Text(
                text = stringResource(id = CoreR.string.categories),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        items(
            items = uiState.categories,
            key = { category -> category.id }
        ) { category ->
            ComparisonCategoryComponent(
                category = category,
                onClick = { onCategoryClick(category) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isInternetAvailable
            )
        }
    }
}

@Composable
@AllPreviewsNightLight
private fun ComparisonQuizListScreenPreview() {
    NewQuizTheme {
        Surface {
            ComparisonQuizListScreenImpl(
                uiState = ComparisonQuizListScreenUiState(
                    categories = listOf(
                        ComparisonQuizCategory(
                            id = "test",
                            description = "Description",
                            title = "Title",
                            imageUrl = "",
                            questionDescription = ComparisonQuizCategory.QuestionDescription(
                                greater = "Greater",
                                less = "Less"
                            ),
                            formatType = ComparisonQuizFormatType.Number
                        )
                    )
                ),
                onCategoryClick = {},
                onSelectMode = {}
            )
        }
    }
}
