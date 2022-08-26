package com.infinitepower.newquiz.multi_choice_quiz.list.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.destinations.SavedMultiChoiceQuestionsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

internal fun getMultiChoiceQuizListCardItemData(
    navigator: DestinationsNavigator,
    savedQuestionsText: String
) = listOf(
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
        description = savedQuestionsText
    ),
    HomeCardItem.GroupTitle(title = CoreR.string.categories)
)