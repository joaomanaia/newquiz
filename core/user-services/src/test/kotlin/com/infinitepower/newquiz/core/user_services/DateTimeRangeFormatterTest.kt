package com.infinitepower.newquiz.core.user_services

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.TimestampWithXP
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

internal class DateTimeRangeFormatterTest {
    @Test
    fun `test day formatter aggregateResults`() {
        val now = Clock.System.now()

        val results = listOf(
            TimestampWithXP(timestamp = now.toEpochMilliseconds(), value = 10),
            TimestampWithXP(timestamp = now.toEpochMilliseconds(), value = 5),
            TimestampWithXP(timestamp = (now - 1.hours).toEpochMilliseconds(), value = 20),
            TimestampWithXP(timestamp = (now - 6.hours).toEpochMilliseconds(), value = 30),
            TimestampWithXP(timestamp = (now - 3.hours).toEpochMilliseconds(), value = 40),
            TimestampWithXP(timestamp = (now - 1.days - 3.hours).toEpochMilliseconds(), value = 40),
        )

        val formatter = DateTimeRangeFormatter.Day
        val resultsAggregated = formatter.aggregateResults(results)

        assertThat(resultsAggregated).hasSize(4)
    }

    @Test
    fun `test week formatter aggregateResults`() {
        val now = Clock.System.now()

        val results = listOf(
            TimestampWithXP(timestamp = now.toEpochMilliseconds(), value = 10),
            TimestampWithXP(timestamp = now.toEpochMilliseconds(), value = 5),
            TimestampWithXP(timestamp = (now - 1.hours).toEpochMilliseconds(), value = 20),
            TimestampWithXP(timestamp = (now - 32.hours).toEpochMilliseconds(), value = 30),
            TimestampWithXP(timestamp = (now - 6.days).toEpochMilliseconds(), value = 40),
        )

        val formatter = DateTimeRangeFormatter.Week
        val resultsAggregated = formatter.aggregateResults(results)

        assertThat(resultsAggregated).hasSize(3)
    }

    @Test
    fun `test getNowDateTimeRange`() {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()

        val today = DateTimeRangeFormatter.Day.getNowDateTimeRange(now, tz)

        val todayStart = today.first.toLocalDateTime(tz).date.atStartOfDayIn(tz)
        assertThat(today.first).isEqualTo(todayStart)
        assertThat(today.second).isEqualTo(now)

        val thisWeek = DateTimeRangeFormatter.Week.getNowDateTimeRange(now, tz)

        val thisWeekStart = now - 7.days
        assertThat(thisWeek.first).isEqualTo(thisWeekStart)
        assertThat(thisWeek.second).isEqualTo(now)
    }
}
