package com.infinitepower.newquiz.core.util.kotlin

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BooleanExtensionsTest {
    @Test
    @DisplayName("Should return 1 when the boolean is true")
    fun toIntWhenBooleanIsTrue() {
        val boolean = true
        val expected = 1
        val actual = boolean.toInt()
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    @DisplayName("Should return 0 when the boolean is false")
    fun toIntWhenBooleanIsFalse() {
        val boolean = false
        val expected = 0
        val actual = boolean.toInt()
        assertThat(expected).isEqualTo(actual)
    }
}