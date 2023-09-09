package com.infinitepower.newquiz.core.util.kotlin

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.random.Random
import kotlin.random.nextULong
import kotlin.test.Test

internal class MathTest {
    @ParameterizedTest(name = "roundToUInt({0}) = {1}")
    @CsvSource("0.0, 0", "0.1, 0", "0.5, 1", "0.9, 1", "1.0, 1", "-1.0, -1")
    fun testRoundToUInt(value: Double, expected: UInt) {
        assertThat(value.roundToUInt()).isEqualTo(expected)
    }

    @ParameterizedTest(name = "{0} pow {1} = {2}")
    @CsvSource("0, 0, 1", "0, 1, 0", "1, 0, 1", "1, 1, 1", "2, 2, 4", "2, 3, 8", "2, 4, 16")
    fun testUIntPow(value: UInt, n: Int, expected: UInt) {
        assertThat(value pow n).isEqualTo(expected)
    }

    @Test
    fun `test ULong div Float`() {
        val randomDividend = Random.nextULong()
        val randomDivisor = Random.nextFloat()

        val expected = randomDividend.toLong() / randomDivisor

        assertThat(randomDividend / randomDivisor).isEqualTo(expected)
    }

    @Test
    fun `test ULong div Double`() {
        val randomDividend = Random.nextULong()
        val randomDivisor = Random.nextDouble()

        val expected = randomDividend.toLong() / randomDivisor
        assertThat(randomDividend / randomDivisor).isEqualTo(expected)
    }
}
