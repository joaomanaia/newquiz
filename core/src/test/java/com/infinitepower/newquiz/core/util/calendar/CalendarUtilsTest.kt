package com.infinitepower.newquiz.core.util.calendar

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Month
import kotlinx.datetime.toLocalDate
import org.junit.jupiter.api.Test

internal class CalendarUtilsTest {
    @Test
    fun getToDoubleDigit() {
        repeat(100) { i ->
            val expected = if (i < 10) "0$i" else i.toString()
            assertThat(i.toDoubleDigit()).isEqualTo(expected)
        }
    }

    @Test
    fun `localDate getMonthAllDays`() {
        val date = "2022-08-06".toLocalDate()
        val days = date.getMonthAllDays()

        assertThat(days).isNotEmpty()

        days.forEachIndexed { index, day ->
            assertThat(day.year).isEqualTo(2022)
            assertThat(day.month).isEqualTo(Month.AUGUST)
            assertThat(day.dayOfMonth).isEqualTo(index + 1)
        }
    }
}