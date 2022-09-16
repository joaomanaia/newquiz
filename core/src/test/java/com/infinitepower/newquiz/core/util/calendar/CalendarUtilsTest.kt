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

        //val dateLeapYear = "2024-02-06".toLocalDate()
    }

    @Test
    fun `normal february month year lengthLowVersionCode, returns 28`() {
        val dateLeapYear = "2022-02-06".toLocalDate()
        val days = dateLeapYear.month.lengthLowVersionCode(false)

        assertThat(days).isEqualTo(28)
    }

    @Test
    fun `leap february month year lengthLowVersionCode, returns 29`() {
        val dateLeapYear = "2024-02-06".toLocalDate()
        val days = dateLeapYear.month.lengthLowVersionCode(true)

        assertThat(days).isEqualTo(29)
    }
}