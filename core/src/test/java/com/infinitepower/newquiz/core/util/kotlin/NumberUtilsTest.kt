package com.infinitepower.newquiz.core.util.kotlin

import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

internal class NumberUtilsTest {
    @Test
    fun `converts single digit to double digit`() {
        val digit = 5
        val result = digit.toDoubleDigit()
        assertThat(result).isEqualTo("05")
    }

    @Test
    fun `does not convert double digit`() {
        val digit = 15
        val result = digit.toDoubleDigit()
        assertThat(result).isEqualTo("15")
    }

    @Test
    fun `multiplies uInt by float`() {
        val uInt = 10u
        val multiplierFactor = 1.5f
        val result = uInt * multiplierFactor
        assertThat(result).isEqualTo(15u)
    }

    @Test
    fun `multiplies uIntRange by float`() {
        val uIntRange = 10u..20u
        val multiplierFactor = 1.5f
        val result = uIntRange * multiplierFactor
        assertThat(result).isEqualTo(15u..30u)
    }
}