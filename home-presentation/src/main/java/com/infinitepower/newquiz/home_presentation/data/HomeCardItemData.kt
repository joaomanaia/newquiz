package com.infinitepower.newquiz.home_presentation.data

import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.home_presentation.HomeScreenNavigator
import com.infinitepower.newquiz.home_presentation.model.CardIcon
import com.infinitepower.newquiz.home_presentation.model.HomeCardItem
import com.infinitepower.newquiz.core.R as CoreR

internal class HomeCardItemData(
    homeNavigator: HomeScreenNavigator
) {
    val items = listOf(
        HomeCardItem.GroupTitle(
            title = CoreR.string.quick_quiz,
        ),
        HomeCardItem.LargeCard(
            title = CoreR.string.quick_quiz,
            icon = CardIcon.Lottie(LottieCompositionSpec.RawRes(CoreR.raw.quick_quiz)),
            onClick = homeNavigator::navigateToQuickQuiz
        )
    )
}