package com.infinitepower.newquiz.core.util.kotlin

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionUtilsTest {
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

    @Test
    fun testGenerateRandomUniqueItems_correctSize() = runTest  {
        val questionSize = 5
        val generator: () -> Int = { Random.nextInt() }

        val items = generateRandomUniqueItems(questionSize, generator)

        assertThat(items).hasSize(questionSize)
        assertThat(items).containsNoDuplicates()
    }

    @Test
    fun testGenerateRandomUniqueItems_generatedItems() = runTest  {
        val questionSize = 5
        val expectedItems = listOf(1, 2, 3, 4, 5)
        var index = 0
        val generator: () -> Int = {
            val item = expectedItems[index]
            index++
            item
        }

        val items = generateRandomUniqueItems(questionSize, generator)

        assertThat(items).containsExactlyElementsIn(expectedItems)
    }

    @Test
    fun testGenerateRandomUniqueItems_sameGenerated_returnsOneSize() = runTest {
        val questionSize = 5
        val generator: () -> Int = { 0 }

        val items = generateRandomUniqueItems(questionSize, generator, 1000)

        assertThat(items).hasSize(1)
        assertThat(items).containsNoDuplicates()
    }
}