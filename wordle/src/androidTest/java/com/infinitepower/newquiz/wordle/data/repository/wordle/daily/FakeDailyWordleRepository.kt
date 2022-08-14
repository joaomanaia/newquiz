package com.infinitepower.newquiz.wordle.data.repository.wordle.daily

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeDailyWordleRepository @Inject constructor() : DailyWordleRepository {
    override suspend fun getAllCalendarItems(): List<WordleDailyCalendarItem> {
        return emptyList()
    }

    override fun getCalendarItems(
        wordSize: Int,
        month: Month,
        year: Int
    ): FlowResource<List<WordleDailyCalendarItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCalendarItem(item: WordleDailyCalendarItem) {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }

    override fun getAllDailyWords(
        wordSize: Int,
        month: Month,
        year: Int
    ): FlowResource<List<WordleDailyItem>> {
        TODO("Not yet implemented")
    }

    override fun getDailyWord(date: LocalDate, wordSize: Int): FlowResource<String> {
        TODO("Not yet implemented")
    }

    override fun getAvailableDailyWords(): FlowResource<Int> {
        TODO("Not yet implemented")
    }
}