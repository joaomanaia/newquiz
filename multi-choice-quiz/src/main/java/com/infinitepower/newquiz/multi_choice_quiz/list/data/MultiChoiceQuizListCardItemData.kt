package com.infinitepower.newquiz.multi_choice_quiz.list.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.ui.home_card.data.CardItemDataCore
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import com.infinitepower.newquiz.multi_choice_quiz.categories.components.CategoryComponent
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.SavedMultiChoiceQuestionsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

internal class MultiChoiceQuizListCardItemData(
    navigator: DestinationsNavigator,
    savedQuestionsSize: Int
) : CardItemDataCore {
    @OptIn(ExperimentalMaterial3Api::class)
    override val items = listOf(
        HomeCardItem.GroupTitle(
            title = CoreR.string.quick_quiz,
        ),
        HomeCardItem.LargeCard(
            title = CoreR.string.quick_quiz,
            icon = CardIcon.Lottie(LottieCompositionSpec.RawRes(CoreR.raw.quick_quiz)),
            onClick = { navigator.navigate(MultiChoiceQuizScreenDestination()) }
        ),
        HomeCardItem.GroupTitle(
            title = CoreR.string.saved_questions,
        ),
        HomeCardItem.MediumCard(
            title = CoreR.string.saved_questions,
            icon = CardIcon.Icon(Icons.Rounded.Save),
            onClick = { navigator.navigate(SavedMultiChoiceQuestionsScreenDestination) },
            description = "$savedQuestionsSize questions available"
        ),
        HomeCardItem.HorizontalItems(
            title = CoreR.string.quick_quiz,
            items = multiChoiceQuestionCategories,
            itemContent = { category ->
                CategoryComponent(
                    category = category,
                    onClick = {}
                )
            }
        )
    )
}