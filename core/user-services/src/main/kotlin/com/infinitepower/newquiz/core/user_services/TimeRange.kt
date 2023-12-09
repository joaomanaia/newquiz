package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.core.database.dao.GameResultDao
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Duration.Companion.days

sealed interface TimeRange {
    fun groupingKey(timestamp: Long): Int

    fun getTimeRange(): Pair<Instant, Instant>

    fun aggregateResults(
        xpForDateRange: List<GameResultDao.XpForPlayedAt>
    ): Map<Int, Int> {
        return xpForDateRange
            .groupBy { groupingKey(it.playedAt) }
            .mapValues { (_, xpForDay) ->
                xpForDay.sumOf { it.earnedXp }
            }.toSortedMap()
    }

    fun formatValueToString(value: Int): String

    companion object {
        private val tz = TimeZone.currentSystemDefault()

        private val todayFormatter by lazy { DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT) }

        private val thisWeekFormatter by lazy { DateTimeFormatter.ofPattern("d MMM") }
    }

    data object Today : TimeRange {
        override fun groupingKey(timestamp: Long): Int {
            return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(tz).hour
        }

        override fun getTimeRange(): Pair<Instant, Instant> {
            val now = Clock.System.now()
            val start = now.toLocalDateTime(tz).date.atStartOfDayIn(tz)

            return Pair(start, now)
        }

        /**
         * @param value hour of the day
         */
        override fun formatValueToString(value: Int): String {
            return LocalTime(value, 0).toJavaLocalTime().format(todayFormatter)
        }
    }

    data object ThisWeek : TimeRange {
        override fun groupingKey(timestamp: Long): Int {
            return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(tz).date.toEpochDays()
        }

        override fun getTimeRange(): Pair<Instant, Instant> {
            val now = Clock.System.now()
            val start = now - 7.days

            return Pair(start, now)
        }

        override fun formatValueToString(value: Int): String {
            return LocalDate.fromEpochDays(value).toJavaLocalDate().format(thisWeekFormatter)
        }
    }
}
