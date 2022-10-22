package com.infinitepower.newquiz.home_presentation.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Flag
import androidx.compose.material.icons.rounded.LogoDev
import androidx.compose.material.icons.rounded.QuestionMark
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.infinitepower.newquiz.home_presentation.HomeScreenNavigator
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.core.R as CoreR

fun getHomeCardItemData(
    homeNavigator: HomeScreenNavigator
) = listOf(
    HomeCardItem.GroupTitle(
        title = CoreR.string.multi_choice_quiz,
    ),
    HomeCardItem.LargeCard(
        title = CoreR.string.quick_quiz,
        icon = CardIcon.Lottie(LottieCompositionSpec.RawRes(CoreR.raw.quick_quiz)),
        onClick = homeNavigator::navigateToQuickQuiz
    ),
    HomeCardItem.GroupTitle(
        title = CoreR.string.wordle,
    ),
    HomeCardItem.LargeCard(
        title = CoreR.string.wordle_infinite,
        icon = CardIcon.Icon(Icons.Rounded.QuestionMark),
        onClick = homeNavigator::navigateToWordle
    ),
    HomeCardItem.GroupTitle(
        title = CoreR.string.flag_quiz,
    ),
    HomeCardItem.LargeCard(
        title = CoreR.string.flag_quiz,
        icon = CardIcon.Icon(Icons.Rounded.Flag),
        onClick = homeNavigator::navigateToFlagQuiz
    ),
    HomeCardItem.GroupTitle(
        title = CoreR.string.logo_quiz,
    ),
    HomeCardItem.LargeCard(
        title = CoreR.string.logo_quiz,
        icon = CardIcon.Icon(Icons.Rounded.Android),
        onClick = homeNavigator::navigateToLogoQuiz
    ),
)