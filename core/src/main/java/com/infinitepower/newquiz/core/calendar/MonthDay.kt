package com.infinitepower.newquiz.core.calendar

import kotlinx.datetime.LocalDate

interface MonthDay {
    object Empty : MonthDay {
        override fun toString(): String = ""
    }

    data class Day(
        val date: LocalDate
    ) : MonthDay {
        override fun toString(): String = date.dayOfMonth.toString()
    }
}