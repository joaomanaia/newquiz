package com.infinitepower.newquiz.wordle.list.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.Today
import com.infinitepower.newquiz.core.R
import com.infinitepower.newquiz.core.ui.home_card.model.CardIcon
import com.infinitepower.newquiz.core.ui.home_card.model.HomeCardItem
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
)