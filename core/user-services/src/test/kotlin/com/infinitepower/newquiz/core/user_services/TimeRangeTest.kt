package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.core.database.dao.GameResultDao
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

internal class TimeRangeTest {
    @Test
    fun testToday() {
        val now = Clock.System.now()

        val gameResults = listOf(
            GameResultDao.XpForPlayedAt(10, now.toEpochMilliseconds()),
            GameResultDao.XpForPlayedAt(10, (now - 1.hours).toEpochMilliseconds()),
            GameResultDao.XpForPlayedAt(20, (now - 6.hours).toEpochMilliseconds()),
            GameResultDao.XpForPlayedAt(30, (now - 3.hours).toEpochMilliseconds()),
        )

        val selectedTimeRange = TimeRange.Today // Change this to TimeRange.ThisWeek if needed

        val filteredResults = selectedTimeRange.aggregateResults(gameResults)

        println(filteredResults)
    }

    @Test
    fun testThisWeek() {
        val now = Clock.System.now()

        val gameResults = listOf(
            GameResultDao.XpForPlayedAt(10, now.toEpochMilliseconds()),
            GameResultDao.XpForPlayedAt(10, (now - 1.hours).toEpochMilliseconds()),
            GameResultDao.XpForPlayedAt(20, (now - 6.hours).toEpochMilliseconds()),
            GameResultDao.XpForPlayedAt(30, (now - 3.days).toEpochMilliseconds()),
        )

        val selectedTimeRange = TimeRange.ThisWeek // Change this to TimeRange.ThisWeek if needed

        val filteredResults = selectedTimeRange.aggregateResults(gameResults)

        println(filteredResults)
    }
}
