package com.infinitepower.newquiz.core.util.kotlin

import com.google.common.truth.Truth
import org.junit.jupiter.api.Test

internal class NumberUtilsTest {
    @Test
    fun `converts single digit to double digit`() {
        val digit = 5
        val result = digit.toDoubleDigit()
        Truth.assertThat(result).isEqualTo("05")
    }

    @Test
    fun `does not convert double digit`() {
        val digit = 15
        val result = digit.toDoubleDigit()
        Truth.assertThat(result).isEqualTo("15")
    }
}