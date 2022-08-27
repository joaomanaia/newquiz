package com.infinitepower.newquiz.multi_choice_quiz.list.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.data.local.multi_choice_quiz.MultiChoiceQuizDifficulty
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceCategoriesScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.SavedMultiChoiceQuestionsScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.list.MultiChoiceCategoriesSelector
import com.infinitepower.newquiz.multi_choice_quiz.list.components.CardDifficulty
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@ExperimentalMaterial3Api
internal fun getMultiChoiceQuizListCardItemData(
    navigator: DestinationsNavigator,
    savedQuestionsText: String,
    recentCategories: List<MultiChoiceQuestionCategory>
) = listOf(
    HomeCardItem.GroupTitle(
        title = CoreR.string.quick_quiz,
    ),
    HomeCardItem.LargeCard(
        title = CoreR.string.quick_quiz,
        icon = CardIcon.Lottie(LottieCompositionSpec.RawRes(CoreR.raw.quick_quiz)),
        backgroundPrimary = true,
        onClick = { navigator.navigate(MultiChoiceQuizScreenDestination()) }
    ),
    HomeCardItem.GroupTitle(title = CoreR.string.difficulty),
    HomeCardItem.HorizontalItems(
        items = MultiChoiceQuizDifficulty.items(),
        itemContent = { difficulty ->
            CardDifficulty(
                multiChoiceQuizDifficulty = difficulty,
                onClick = {
                    navigator.navigate(MultiChoiceQuizScreenDestination(difficulty = difficulty.id))
                }
            )
        }
    ),
    HomeCardItem.GroupTitle(title = CoreR.string.categories),
    HomeCardItem.CustomItem(
        content = {
            MultiChoiceCategoriesSelector(
                recentCategories = recentCategories,
                navigateToCategoriesScreen = {
                    navigator.navigate(MultiChoiceCategoriesScreenDestination)
                },
                navigateToQuizScreen = { categoryId ->
                    navigator.navigate(MultiChoiceQuizScreenDestination(category = categoryId))
                }
            )
        }
    ),
    HomeCardItem.GroupTitle(title = CoreR.string.saved_questions),
    HomeCardItem.MediumCard(
        title = CoreR.string.saved_questions,
        icon = CardIcon.Icon(Icons.Rounded.Save),
        onClick = { navigator.navigate(SavedMultiChoiceQuestionsScreenDestination) },
        description = savedQuestionsText
    ),
)