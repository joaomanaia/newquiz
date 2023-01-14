package com.infinitepower.newquiz.core.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarMonthViewImpl @Inject constructor() : MonthView {
    private val calendar = Calendar.getInstance()

    constructor(year: Int, month: Int) : this() {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        setToFirstDayOfMonth()
    }

    init {
        setToFirstDayOfMonth()
    }

    override val currentYear: Int
        get() = calendar.get(Calendar.YEAR)

    override val currentMonth: Month
        get() {
            // plus 1 because in calendar the range of months goes 0 to 11 and is required 1 to 12
            val monthNumber = calendar.get(Calendar.MONTH) + 1
            return Month(monthNumber)
        }

    private fun setToFirstDayOfMonth() {
        calendar.set(Calendar.DAY_OF_MONTH, 1)
    }

    override fun generateCalendarDays(): List<MonthDay> {
        val maxMonthDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val startWeekday = calendar.get(Calendar.DAY_OF_WEEK)

        val year = currentYear
        val month = currentMonth

        return buildList {
            // Generate empty start days
            for (i in 1 until startWeekday) {
                add(MonthDay.Empty)
            }

            for (day in 1..maxMonthDays) {
                val dayDate = LocalDate(year, month, day)
                add(MonthDay.Day(dayDate))
            }
        }
    }

    override fun nextMonth(increase: Int) {
        calendar.add(Calendar.MONTH, increase)
        setToFirstDayOfMonth()
    }

    override fun previousMonth(decrease: Int) {
        calendar.add(Calendar.MONTH, -decrease)
        setToFirstDayOfMonth()
    }
}