package com.infinitepower.newquiz.multi_choice_quiz.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.core.analytics.logging.rememberCoreLoggingAnalytics
import com.infinitepower.newquiz.core.common.annotation.compose.AllPreviewsNightLight
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.core.theme.spacing
import com.infinitepower.newquiz.core.ui.components.category.CategoryComponent
import com.infinitepower.newquiz.core.ui.components.rememberIsInternetAvailable
import com.infinitepower.newquiz.core.ui.home_card.components.HomeGroupTitle
import com.infinitepower.newquiz.core.ui.home_card.components.HomeLargeCard
import com.infinitepower.newquiz.core.ui.home_card.components.HomeMediumCard
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.model.multi_choice_quiz.toBaseCategory
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.multi_choice_quiz.components.difficulty.SelectableDifficultyRow
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.SavedMultiChoiceQuestionsScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.list.util.MultiChoiceQuizListUtils
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

    val coreLoggingAnalytics = rememberCoreLoggingAnalytics()
    LaunchedEffect(key1 = true) {
        coreLoggingAnalytics.logScreenView("MultiChoiceListScreen")
    }
}

@Composable
@ExperimentalMaterial3Api
private fun MultiChoiceQuizListScreenImpl(
    uiState: MultiChoiceQuizListScreenUiState,
    navigator: DestinationsNavigator
) {
    val spaceMedium = MaterialTheme.spacing.medium

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

    val seeAllText = if (seeAllCategories) {
        stringResource(id = CoreR.string.see_less_categories)
    } else {
        stringResource(id = CoreR.string.see_all_categories)
    }

    val seeAllIcon = if (seeAllCategories) {
        Icons.Rounded.ExpandLess
    } else {
        Icons.Rounded.ExpandMore
    }

    val allCategories = remember(isInternetAvailable) {
        MultiChoiceQuizListUtils.getAllCategories(isInternetAvailable = isInternetAvailable)
    }

    val recentCategories = remember(uiState.recentCategories, isInternetAvailable) {
        MultiChoiceQuizListUtils.getRecentCategories(
            recentCategories = uiState.recentCategories,
            allCategories = multiChoiceQuestionCategories,
            isInternetAvailable = isInternetAvailable
        )
    }

    // The other categories are the ones that are not recent
    val otherCategories = remember(allCategories, recentCategories) {
        allCategories - recentCategories.toSet()
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

        items(
            items = recentCategories,
            key = { category -> "recent_category_${category.id}" }
        ) { category ->
            CategoryComponent(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(120.dp),
                title = category.name.asString(),
                imageUrl = category.image,
                onClick = {
                    navigator.navigate(
                        MultiChoiceQuizScreenDestination(
                            category = category.toBaseCategory(),
                            difficulty = selectedDifficulty?.id
                        )
                    )
                },
                enabled = isInternetAvailable || !category.requireInternetConnection
            )
        }

        item {
            Box(
                modifier = Modifier.fillParentMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { seeAllCategories = !seeAllCategories }
                ) {
                    Icon(
                        imageVector = seeAllIcon,
                        contentDescription = seeAllText,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                    Text(
                        text = seeAllText,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (seeAllCategories) {
            items(
                items = otherCategories,
                key = { category -> category.id }
            ) { category ->
                CategoryComponent(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(120.dp),
                    title = category.name.asString(),
                    imageUrl = category.image,
                    onClick = {
                        navigator.navigate(
                            MultiChoiceQuizScreenDestination(
                                category = category.toBaseCategory(),
                                difficulty = selectedDifficulty?.id
                            )
                        )
                    },
                    enabled = isInternetAvailable || !category.requireInternetConnection
                )
            }
        }

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
fun MultiChoiceCategoriesPreview() {
    NewQuizTheme {
        Surface {
            MultiChoiceQuizListScreenImpl(
                uiState = MultiChoiceQuizListScreenUiState(),
                navigator = EmptyDestinationsNavigator
            )
        }
    }
}
