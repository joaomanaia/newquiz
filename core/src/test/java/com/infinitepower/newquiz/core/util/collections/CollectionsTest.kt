package com.infinitepower.newquiz.core.util.collections

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class IndexOfFirstOrNullTest {
    @Test
    @DisplayName("Should return the index of the first element that satisfies the predicate")
    fun indexOfFirstOrNullWhenPredicateIsSatisfiedThenReturnTheIndex() {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.indexOfFirstOrNull { it == 2 }

        assertThat(result).isEqualTo(1)
    }

    @Test
    @DisplayName("Should return null when the predicate is not satisfied")
    fun indexOfFirstOrNullWhenPredicateIsNotSatisfiedThenReturnNull() {
        val list = listOf(1, 2, 3, 4, 5)
        val result = list.indexOfFirstOrNull { it == 6 }

        assertThat(result).isNull()
    }
}