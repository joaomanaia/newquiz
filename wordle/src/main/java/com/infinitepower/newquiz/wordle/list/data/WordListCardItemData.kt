package com.infinitepower.newquiz.wordle.list.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Numbers
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Today
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.wordle.destinations.DailyWordleCalendarScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

fun getWordListCardItemData(
    navigator: DestinationsNavigator
) = listOf(
    HomeCardItem.GroupTitle(
        title = R.string.wordle_infinite,
    ),
    HomeCardItem.LargeCard(
        title = R.string.wordle_infinite,
        icon = CardIcon.Icon(Icons.Rounded.Quiz),
        onClick = {
            navigator.navigate(WordleScreenDestination())
        },
        backgroundPrimary = true
    ),
    HomeCardItem.GroupTitle(
        title = R.string.wordle_daily,
    ),
    HomeCardItem.LargeCard(
        title = R.string.wordle_daily,
        icon = CardIcon.Icon(Icons.Rounded.Today),
        onClick = {
            navigator.navigate(DailyWordleCalendarScreenDestination)
        }
    ),
    HomeCardItem.GroupTitle(title = R.string.number_trivia),
    HomeCardItem.LargeCard(
        title = R.string.number_trivia,
        icon = CardIcon.Icon(Icons.Rounded.Numbers),
        onClick = { navigator.navigate(WordleScreenDestination(quizType = WordleQuizType.NUMBER_TRIVIA)) },
        requireInternetConnection = true
    ),
    HomeCardItem.GroupTitle(
        title = R.string.guess_number,
    ),
    HomeCardItem.LargeCard(
        title = R.string.guess_number,
        icon = CardIcon.Icon(Icons.Rounded.Numbers),
        onClick = {
            navigator.navigate(WordleScreenDestination(quizType = WordleQuizType.NUMBER))
        }
    ),
    HomeCardItem.GroupTitle(
        title = R.string.guess_math_formula,
    ),
    HomeCardItem.LargeCard(
        title = R.string.guess_math_formula,
        icon = CardIcon.Icon(Icons.Rounded.Numbers),
        onClick = {
            navigator.navigate(WordleScreenDestination(quizType = WordleQuizType.MATH_FORMULA))
        }
    ),
)