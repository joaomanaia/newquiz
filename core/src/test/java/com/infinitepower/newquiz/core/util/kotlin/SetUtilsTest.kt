package com.infinitepower.newquiz.core.util.kotlin

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class SetUtilsTest {
    @Test
    fun `Test Set removeFirst`() {
        val set = setOf("apple", "banana", "cherry")
        val result = set.removeFirst()
        assertThat(result).containsExactly("banana", "cherry")
    }

    @Test
    fun `Test Set removeLast`() {
        val set = setOf("apple", "banana", "cherry")
        val result = set.removeLast()
        assertThat(result).containsExactly("apple", "banana")
    }
}