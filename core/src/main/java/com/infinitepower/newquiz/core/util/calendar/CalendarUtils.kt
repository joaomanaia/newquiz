package com.infinitepower.newquiz.core.util.calendar

import android.os.Build
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toLocalDate

fun Int.toDoubleDigit(): String = String.format("%02d", this)

fun LocalDate.getMonthAllDays(): List<LocalDate> {
    val numDays = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        month.length(false)
    } else {
        month.lengthLowVersionCode(false)
    }
    return List(numDays) { index ->
        val day = index + 1
        "$year-${monthNumber.toDoubleDigit()}-${day.toDoubleDigit()}".toLocalDate()
    }
}

private fun Month.lengthLowVersionCode(leapYear: Boolean): Int {
    return when (ordinal + 1) {
        2 -> if (leapYear) 29 else 28
        4,
        6,
        9,
        11 -> 30
        else -> 31
    }
}