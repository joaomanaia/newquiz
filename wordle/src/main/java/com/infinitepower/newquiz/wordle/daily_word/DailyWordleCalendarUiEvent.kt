package com.infinitepower.newquiz.wordle.daily_word

import kotlinx.datetime.LocalDate

sealed interface DailyWordleCalendarUiEvent {
    data class OnChangePageIndex(
        val index: Int
    ) : DailyWordleCalendarUiEvent

    object NextMonth : DailyWordleCalendarUiEvent

    object PreviousMonth : DailyWordleCalendarUiEvent

    data class OnDateClick(
        val date: LocalDate
    ) : DailyWordleCalendarUiEvent
}
