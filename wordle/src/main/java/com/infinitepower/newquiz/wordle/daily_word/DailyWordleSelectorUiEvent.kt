package com.infinitepower.newquiz.wordle.daily_word

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

sealed class DailyWordleSelectorUiEvent {
    data class OnChangePageIndex(
        val index: Int
    ) : DailyWordleSelectorUiEvent()

    data class OnChangeInstant(
        val instant: Instant
    ) : DailyWordleSelectorUiEvent()

    data class OnDateClick(
        val date: LocalDate
    ) : DailyWordleSelectorUiEvent()
}
