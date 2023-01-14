package com.infinitepower.newquiz.core.calendar

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.Month
import org.junit.jupiter.api.Test
import java.util.Calendar

internal class CalendarMonthViewImplTest {
    @Test
    fun `generateCalendarDays returns a list of CalendarDay`() {
        val year = 2023
        val month = Calendar.JANUARY
        val calendarTable = CalendarMonthViewImpl(year, month)
        val calendarDays = calendarTable.generateCalendarDays()

        assertThat(calendarDays).isNotNull()
        assertThat(calendarDays).isNotEmpty()
        assertThat(calendarDays).hasSize(31)
        assertThat(calendarDays.all { it is MonthDay.Day }).isTrue()

        val allDays = calendarDays.filterIsInstance<MonthDay.Day>().map { it.date.dayOfMonth }
        assertThat(allDays).isInOrder()
    }

    @Test
    fun `generateCalendarDays with 2023 and february, test empty days`() {
        val year = 2023
        val month = Calendar.FEBRUARY
        val calendarTable = CalendarMonthViewImpl(year, month)
        val calendarDays = calendarTable.generateCalendarDays()

        assertThat(calendarDays).isNotNull()
        assertThat(calendarDays).isNotEmpty()
        assertThat(calendarDays).hasSize(31)
        assertThat(calendarDays.filterIsInstance<MonthDay.Empty>()).hasSize(3)
    }

    @Test
    fun `nextMonth increases the month`() {
        val year = 2023
        val month = Calendar.JANUARY
        val calendarTable = CalendarMonthViewImpl(year, month)
        calendarTable.nextMonth()

        assertThat(calendarTable.currentYear).isEqualTo(2023)
        assertThat(calendarTable.currentMonth).isEqualTo(Month.FEBRUARY)
    }

    @Test
    fun `previousMonth decreases the month`() {
        val year = 2023
        val month = Calendar.JANUARY
        val calendarTable = CalendarMonthViewImpl(year, month)
        calendarTable.previousMonth()

        assertThat(calendarTable.currentYear).isEqualTo(2022)
        assertThat(calendarTable.currentMonth).isEqualTo(Month.DECEMBER)
    }
}