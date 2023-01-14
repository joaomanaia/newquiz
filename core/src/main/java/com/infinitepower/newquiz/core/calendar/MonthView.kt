package com.infinitepower.newquiz.core.calendar

import kotlinx.datetime.Month

interface MonthView {
    val currentYear: Int

    val currentMonth: Month

    fun nextMonth(increase: Int = 1)

    fun previousMonth(decrease: Int = 1)

    fun generateCalendarDays(): List<MonthDay>
}