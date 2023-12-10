package com.infinitepower.newquiz.core.user_services.data.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.BeforeTest

/**
 * Tests for [ComparisonQuizXpGeneratorImpl].
 */
internal class ComparisonQuizXpGeneratorImplTest {
    private lateinit var comparisonQuizXpGeneratorImpl: ComparisonQuizXpGeneratorImpl

    private val remoteConfig: RemoteConfig = mockk()

    @BeforeTest
    fun setUp() {
        comparisonQuizXpGeneratorImpl = ComparisonQuizXpGeneratorImpl(remoteConfig)

        every { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_DEFAULT_XP_REWARD) } returns 10
    }

    @ParameterizedTest(name = "test getXpForPosition with endPosition = {0} and skippedAnswers = {1}")
    @CsvSource("""
        1, 0
    """)
    fun `test getXpForPosition`(
        endPosition: Int,
        skippedAnswers: Int
    ) {
        val xp = comparisonQuizXpGeneratorImpl.generateXp(
            endPosition = endPosition.toUInt(),
            skippedAnswers = 0u
        )

        val answersNotSkipped = endPosition - skippedAnswers - 1
        val expectedXp = comparisonQuizXpGeneratorImpl.getDefaultXpForAnswer() * answersNotSkipped.toUInt()

        assertThat(xp).isEqualTo(expectedXp)
    }
}