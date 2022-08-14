package com.infinitepower.newquiz.model.wordle.daily

import androidx.annotation.Keep
import androidx.annotation.IntRange
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

enum class CalendarItemState {
    NONE, WON, LOSS;

    companion object {
        fun stateByGameWin(win: Boolean) = if (win) WON else LOSS
    }
}

@Keep
@Entity(tableName = "wordle_daily_calendar")
data class WordleDailyCalendarItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val state: CalendarItemState,
    @IntRange(from = 4, to = 7) val wordSize: Int
)