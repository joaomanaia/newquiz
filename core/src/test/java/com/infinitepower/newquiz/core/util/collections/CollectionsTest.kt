package com.infinitepower.newquiz.core.util.collections

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class CollectionsTest {
    @Test
    fun indexOfFirstOrNull() {
        val list = listOf("A", "B", "C")

        val index = list.indexOfFirstOrNull { it == "B" }
        assertThat(index).isEqualTo(1)

        val indexNull = list.indexOfFirstOrNull { it == "D" }
        assertThat(indexNull).isNull()
    }
}