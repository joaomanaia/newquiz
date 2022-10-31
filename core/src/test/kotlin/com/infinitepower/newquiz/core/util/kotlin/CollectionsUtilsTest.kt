package com.infinitepower.newquiz.core.util.kotlin

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SumOfIntRangeTest {
    @Test
    @DisplayName("Should return the sum of two ranges")
    fun sumOfTwoRanges() {
        val range1 = 1..10
        val range2 = 11..20

        val expected = 12..30

        val result = listOf(range1, range2).sum()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName("Should return the sum of three ranges")
    fun sumOfThreeRanges() {
        val range1 = 1..10
        val range2 = 11..20
        val range3 = 21..30

        val result = listOf(range1, range2, range3).sum()

        assertThat(result).isEqualTo(33..60)
    }
}