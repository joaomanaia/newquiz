package com.infinitepower.newquiz.quiz_presentation.list.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.ui.home_card.data.CardItemDataCore
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.quiz_presentation.destinations.QuizScreenDestination
import com.infinitepower.newquiz.quiz_presentation.destinations.SavedQuestionsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

internal class QuizListCardItemData(
    navigator: DestinationsNavigator
) : CardItemDataCore {
    override val items = listOf(
        HomeCardItem.GroupTitle(
            title = R.string.quick_quiz,
        ),
        HomeCardItem.LargeCard(
            title = R.string.quick_quiz,
            icon = CardIcon.Lottie(LottieCompositionSpec.RawRes(R.raw.quick_quiz)),
            onClick = { navigator.navigate(QuizScreenDestination()) }
        ),
        HomeCardItem.GroupTitle(
            title = R.string.saved_questions,
        ),
        HomeCardItem.LargeCard(
            title = R.string.saved_questions,
            icon = CardIcon.Icon(Icons.Rounded.Save),
            onClick = { navigator.navigate(SavedQuestionsScreenDestination) }
        ),
    )
}