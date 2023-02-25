package com.infinitepower.newquiz.multi_choice_quiz.list.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.question.QuestionDifficulty
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
    recentCategories: List<MultiChoiceCategory>
) = listOf(
    HomeCardItem.GroupTitle(title = CoreR.string.quick_quiz),
    HomeCardItem.LargeCard(
        title = CoreR.string.quick_quiz,
        icon = CardIcon.Lottie(LottieCompositionSpec.RawRes(CoreR.raw.quick_quiz)),
        backgroundPrimary = true,
        onClick = { navigator.navigate(MultiChoiceQuizScreenDestination()) }
    ),
    HomeCardItem.GroupTitle(title = CoreR.string.difficulty),
    HomeCardItem.HorizontalItems(
        items = QuestionDifficulty.items(),
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
                navigateToQuizScreen = { categoryKey ->
                    navigator.navigate(MultiChoiceQuizScreenDestination(category = categoryKey))
                }
            )
        }
    ),
    HomeCardItem.GroupTitle(title = CoreR.string.number_trivia),
    HomeCardItem.LargeCard(
        title = CoreR.string.number_trivia,
        icon = CardIcon.Icon(Icons.Rounded.Numbers),
        onClick = { navigator.navigate(MultiChoiceQuizScreenDestination(category = MultiChoiceBaseCategory.NumberTrivia)) }
    ),
    HomeCardItem.GroupTitle(title = CoreR.string.saved_questions),
    HomeCardItem.MediumCard(
        title = CoreR.string.saved_questions,
        icon = CardIcon.Icon(Icons.Rounded.Save),
        onClick = { navigator.navigate(SavedMultiChoiceQuestionsScreenDestination) },
        description = savedQuestionsText
    ),
)