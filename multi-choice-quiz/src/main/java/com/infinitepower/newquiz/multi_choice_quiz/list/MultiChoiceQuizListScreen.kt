package com.infinitepower.newquiz.multi_choice_quiz.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.LocalAnalyticsHelper
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.home.HomeLazyColumn
import com.infinitepower.newquiz.core.ui.home.homeCategoriesItems
import com.infinitepower.newquiz.core.ui.home_card.components.HomeGroupTitle
import com.infinitepower.newquiz.core.ui.home_card.components.HomeMediumCard
import com.infinitepower.newquiz.core.ui.home_card.components.PlayRandomQuizCard
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.model.multi_choice_quiz.toBaseCategory
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.multi_choice_quiz.components.difficulty.SelectableDifficultyRow
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.SavedMultiChoiceQuestionsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.infinitepower.newquiz.core.R as CoreR

@Composable
@Destination
@OptIn(ExperimentalMaterial3Api::class)
fun MultiChoiceQuizListScreen(
    navigator: DestinationsNavigator,
    viewModel: MultiChoiceQuizListScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MultiChoiceQuizListScreenImpl(
        uiState = uiState,
        navigator = navigator
    )
}

@Composable
@ExperimentalMaterial3Api
private fun MultiChoiceQuizListScreenImpl(
    uiState: MultiChoiceQuizListScreenUiState,
    navigator: DestinationsNavigator
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    val spaceMedium = MaterialTheme.spacing.medium

    val questionsAvailableText = pluralStringResource(
        id = CoreR.plurals.n_questions_available,
        count = uiState.savedQuestionsSize,
        formatArgs = arrayOf(uiState.savedQuestionsSize)
    )

    val (selectedDifficulty, setSelectedDifficulty) = remember {
        // When null, difficulty will be random
        mutableStateOf<QuestionDifficulty?>(null)
    }

    var seeAllCategories by remember { mutableStateOf(false) }

    HomeLazyColumn(
        contentPadding = PaddingValues(
            bottom = MaterialTheme.spacing.large
        )
    ) {
        item {
            HomeGroupTitle(
                title = stringResource(id = CoreR.string.random_quiz),
                modifier = Modifier.padding(horizontal = spaceMedium)
            )
        }

        item {
            PlayRandomQuizCard(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = spaceMedium),
                title = stringResource(id = CoreR.string.quiz_with_random_categories),
                buttonTitle = stringResource(id = CoreR.string.random_quiz),
                containerMainColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navigator.navigate(MultiChoiceQuizScreenDestination(difficulty = selectedDifficulty?.id))
                },
                enabled = uiState.internetConnectionAvailable,
            )
        }

        item {
            Text(
                text = stringResource(id = CoreR.string.difficulty),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = spaceMedium)
            )
        }

        item {
            SelectableDifficultyRow(
                selectedDifficulty = selectedDifficulty,
                setSelectedDifficulty = setSelectedDifficulty
            )
        }

        item {
            Text(
                text = stringResource(id = CoreR.string.categories),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = spaceMedium)
            )
        }

        homeCategoriesItems(
            contentPadding = PaddingValues(
                horizontal = spaceMedium
            ),
            seeAllCategories = seeAllCategories,
            recentCategories = uiState.homeCategories.recentCategories,
            otherCategories = uiState.homeCategories.otherCategories,
            isInternetAvailable = uiState.internetConnectionAvailable,
            showConnectionInfo = uiState.showCategoryConnectionInfo,
            onCategoryClick = { category ->
                analyticsHelper.logEvent(
                    AnalyticsEvent.CategoryClicked(
                        game = AnalyticsEvent.Game.MULTI_CHOICE_QUIZ,
                        categoryId = category.id,
                        otherData = mapOf(
                            "difficulty" to selectedDifficulty?.id
                        )
                    )
                )

                navigator.navigate(
                    MultiChoiceQuizScreenDestination(
                        category = category.toBaseCategory(),
                        difficulty = selectedDifficulty?.id
                    )
                )
            },
            onSeeAllCategoriesClick = { seeAllCategories = !seeAllCategories }
        )

        item {
            HomeGroupTitle(
                title = stringResource(id = CoreR.string.saved_questions),
                modifier = Modifier.padding(horizontal = spaceMedium)
            )
        }

        item {
            HomeMediumCard(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = spaceMedium),
                data = HomeCardItem.MediumCard(
                    title = CoreR.string.saved_questions,
                    icon = CardIcon.Icon(Icons.Rounded.Save),
                    onClick = { navigator.navigate(SavedMultiChoiceQuestionsScreenDestination) },
                    description = questionsAvailableText
                )
            )
        }
    }
}

@Composable
@AllPreviewsNightLight
@OptIn(ExperimentalMaterial3Api::class)
private fun MultiChoiceCategoriesPreview() {
    NewQuizTheme {
        Surface {
            MultiChoiceQuizListScreenImpl(
                uiState = MultiChoiceQuizListScreenUiState(),
                navigator = EmptyDestinationsNavigator
            )
        }
    }
}
