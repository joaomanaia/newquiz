package com.infinitepower.newquiz.core.user_services.data.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
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

    @ParameterizedTest(name = "test getXpForPosition with endPosition = {0}")
    @ValueSource(ints = [0, 1, 2, 3, 4, 5])
    fun `test getXpForPosition`(endPosition: Int) {
        val xp = comparisonQuizXpGeneratorImpl.generateXp(endPosition.toUInt())

        val expectedXp = endPosition.toUInt() * comparisonQuizXpGeneratorImpl.getDefaultXpForAnswer()

        assertThat(xp).isEqualTo(expectedXp)
    }
}