package com.infinitepower.newquiz.wordle.daily_word

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Keep
data class DailyWordleSelectorUiState(
    val pageIndex: Int = 0,
    val calendarItems: List<WordleDailyCalendarItem> = emptyList(),
    val instant: Instant = Clock.System.now()
) {
    val wordSize: Int
        get() = when (pageIndex) {
            0 -> 4
            1 -> 5
            2 -> 6
            else -> throw IllegalStateException()
        }
}