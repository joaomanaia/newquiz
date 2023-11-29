package com.infinitepower.newquiz.core.user_services.data.xp

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.BeforeTest

/**
 * Tests for [ComparisonQuizXpGeneratorImpl].
 */
internal class ComparisonQuizXpGeneratorImplTest {
    private lateinit var comparisonQuizXpGeneratorImpl: ComparisonQuizXpGeneratorImpl

    @BeforeTest
    fun setUp() {
        comparisonQuizXpGeneratorImpl = ComparisonQuizXpGeneratorImpl()
    }

    @ParameterizedTest(name = "test getXpForPosition with endPosition = {0}")
    @ValueSource(ints = [0, 1, 2, 3, 4, 5])
    fun `test getXpForPosition`(endPosition: Int) {
        val xp = comparisonQuizXpGeneratorImpl.generateXp(endPosition.toUInt())

        val expectedXp = endPosition.toUInt() * comparisonQuizXpGeneratorImpl.getDefaultXpForAnswer()

        assertThat(xp).isEqualTo(expectedXp)
    }
}