package com.infinitepower.newquiz.wordle.daily_word

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.calendar.MonthDay
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import kotlinx.datetime.LocalDate

@Keep
data class DailyWordleCalendarUiState(
    val pageIndex: Int = 0,
    val calendarDays: List<MonthDay> = emptyList(),
    val calendarSavedItems: List<WordleDailyCalendarItem> = emptyList()
) {
    val wordSize: Int
        get() = when (pageIndex) {
            0 -> 4
            1 -> 5
            2 -> 6
            else -> throw IllegalStateException()
        }

    val firstDayDate: LocalDate?
        get() = calendarDays
            .filterIsInstance<MonthDay.Day>()
            .firstOrNull()
            ?.date
}