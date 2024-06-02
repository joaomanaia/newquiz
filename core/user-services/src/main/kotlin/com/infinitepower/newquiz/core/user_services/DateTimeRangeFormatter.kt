package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.model.TimestampWithXP
import com.infinitepower.newquiz.model.XP
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

typealias GroupingKey = Int
typealias XPByGroupingKey = Map<GroupingKey, XP>
typealias TimeRange = Pair<Instant, Instant>

sealed interface DateTimeRangeFormatter {
    fun groupingKey(
        timestamp: Long,
        tz: TimeZone = TimeZone.currentSystemDefault()
    ): GroupingKey

    val formatter: DateTimeFormatter

    fun formatValueToString(value: GroupingKey): String

    fun aggregateResults(
        results: List<TimestampWithXP>,
        tz: TimeZone = TimeZone.currentSystemDefault()
    ): XPByGroupingKey {
        return results
            .groupBy { groupingKey(it.timestamp, tz) }
            .mapValues { (_, values) ->
                values.sumOf { it.value }
            }.toSortedMap()
    }

    fun getNowDateTimeRange(
        now: Instant = Clock.System.now(),
        tz: TimeZone = TimeZone.currentSystemDefault()
    ): TimeRange

    data object Day : DateTimeRangeFormatter {
        override fun groupingKey(
            timestamp: Long,
            tz: TimeZone
        ): GroupingKey {
            return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(tz).hour
        }

        override val formatter: DateTimeFormatter by lazy {
            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        }

        override fun formatValueToString(value: GroupingKey): String {
            return LocalTime(value, 0).toJavaLocalTime().format(formatter)
        }

        override fun getNowDateTimeRange(
            now: Instant,
            tz: TimeZone
        ): TimeRange {
            val start = now.toLocalDateTime(tz).date.atStartOfDayIn(tz)

            return start to now
        }
    }

    data object Week : DateTimeRangeFormatter {
        override fun groupingKey(
            timestamp: Long,
            tz: TimeZone
        ): GroupingKey {
            return Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(tz).date.toEpochDays()
        }

        override val formatter: DateTimeFormatter by lazy {
            DateTimeFormatter.ofPattern("d MMM")
        }

        override fun formatValueToString(value: GroupingKey): String {
            return LocalDate.fromEpochDays(value).toJavaLocalDate().format(formatter)
        }

        override fun getNowDateTimeRange(
            now: Instant,
            tz: TimeZone
        ): TimeRange {
            val start = now - 7.days

            return start to now
        }
    }
}
