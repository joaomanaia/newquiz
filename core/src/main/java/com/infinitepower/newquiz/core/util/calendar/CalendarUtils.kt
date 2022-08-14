package com.infinitepower.newquiz.core.util.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

fun Int.toDoubleDigit(): String = String.format("%02d", this)

fun LocalDate.getMonthAllDays(): List<LocalDate> {
    val numDays = month.length(false)
    return List(numDays) { index ->
        val day = index + 1
        "$year-${monthNumber.toDoubleDigit()}-${day.toDoubleDigit()}".toLocalDate()
    }
}