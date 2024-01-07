package com.infinitepower.newquiz.core.user_services.data.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.util.kotlin.roundToUInt
import io.mockk.every
import io.mockk.mockk
import kotlin.math.sqrt
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [WordleXpGeneratorImpl].
 */
internal class WordleXpGeneratorImplTest {
    private lateinit var wordleXpGeneratorImpl: WordleXpGeneratorImpl

    private val remoteConfig: RemoteConfig = mockk()

    @BeforeTest
    fun setUp() {
        wordleXpGeneratorImpl = WordleXpGeneratorImpl(remoteConfig)

        every { remoteConfig.get(RemoteConfigValue.WORDLE_DEFAULT_XP_REWARD) } returns 10
    }

    @Test
    fun `test generateXp`() {
        val rowsUsed = 5u

        val generatedXp = wordleXpGeneratorImpl.generateXp(rowsUsed)

        val defaultXp = wordleXpGeneratorImpl.getDefaultXp()
        val expectedXp = (defaultXp.toInt() * (2 / sqrt(rowsUsed.toDouble()))).roundToUInt()

        assertThat(generatedXp).isEqualTo(expectedXp)
    }
}
