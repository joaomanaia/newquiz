package com.infinitepower.newquiz.domain.repository.wordle.daily

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

interface DailyWordleRepository {
    suspend fun getAllCalendarItems(): List<WordleDailyCalendarItem>

    fun getCalendarItems(
        wordSize: Int,
        month: Month,
        year: Int
    ): FlowResource<List<WordleDailyCalendarItem>>

    suspend fun insertCalendarItem(item: WordleDailyCalendarItem)

    suspend fun clearAll()

    fun getAllDailyWords(
        wordSize: Int,
        month: Month,
        year: Int
    ): FlowResource<List<WordleDailyItem>>

    fun getDailyWord(
        date: LocalDate,
        wordSize: Int
    ): FlowResource<String>

    fun getAvailableDailyWords(): FlowResource<Int>
}