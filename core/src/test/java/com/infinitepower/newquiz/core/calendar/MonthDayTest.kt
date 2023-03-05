package com.infinitepower.newquiz.core.calendar

import com.google.common.truth.Truth.assertThat
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

internal class MonthDayTest {
    @Test
    fun `test monthDay toString`() {
        val emptyString = MonthDay.Empty.toString()
        assertThat(emptyString).isEmpty()

        val day = MonthDay.Day(LocalDate(2023, 2, 5))
        val dayString = day.toString()
        assertThat(dayString).isEqualTo("5")
    }
}