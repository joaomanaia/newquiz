package com.infinitepower.newquiz.multi_choice_quiz.list

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
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.ui.components.rememberIsInternetAvailable
import com.infinitepower.newquiz.core.ui.home.HomeLazyColumn
import com.infinitepower.newquiz.core.ui.home.homeCategoriesItems
import com.infinitepower.newquiz.core.ui.home_card.components.HomeGroupTitle
import com.infinitepower.newquiz.core.ui.home_card.components.HomeLargeCard
import com.infinitepower.newquiz.core.ui.home_card.components.HomeMediumCard
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
    val isInternetAvailable = rememberIsInternetAvailable()

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

    HomeLazyColumn {
        item {
            HomeGroupTitle(title = stringResource(id = CoreR.string.random_quiz))
        }

        item {
            HomeLargeCard(
                modifier = Modifier.fillParentMaxWidth(),
                data = HomeCardItem.LargeCard(
                    title = CoreR.string.quiz_with_random_categories,
                    icon = CardIcon.Lottie(LottieCompositionSpec.RawRes(CoreR.raw.quick_quiz)),
                    backgroundPrimary = true,
                    onClick = {
                        navigator.navigate(MultiChoiceQuizScreenDestination(difficulty = selectedDifficulty?.id))
                    },
                    requireInternetConnection = true
                )
            )
        }

        item {
            Text(
                text = stringResource(id = CoreR.string.difficulty),
                style = MaterialTheme.typography.bodyMedium
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
                style = MaterialTheme.typography.bodyMedium
            )
        }

        homeCategoriesItems(
            seeAllCategories = seeAllCategories,
            recentCategories = uiState.homeCategories.recentCategories,
            otherCategories = uiState.homeCategories.otherCategories,
            isInternetAvailable = isInternetAvailable,
            onCategoryClick = { category ->
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
            HomeGroupTitle(title = stringResource(id = CoreR.string.saved_questions))
        }

        item {
            HomeMediumCard(
                modifier = Modifier.fillParentMaxWidth(),
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
