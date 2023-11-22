package com.infinitepower.newquiz.core.user_services.domain.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.util.kotlin.times
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.test.Test

/**
 * Tests for [XpGenerator].
 */
internal class XpGeneratorTest {
    @Test
    fun `generateRandomXp() should return a random xp`() {
        val xpGenerator = object : XpGenerator {
            override val generatedXpRange: UIntRange = 10u..20u
        }

        val randomXp = xpGenerator.generateRandomXp()

        assertThat(randomXp).isIn(xpGenerator.generatedXpRange)
    }

    @Test
    fun `generateRandomXp() should return a random xp multiplied by the difficulty`() {
        val xpGenerator = object : XpGenerator {
            override val generatedXpRange: UIntRange = 10u..20u
        }

        // Test easy difficulty
        val randomEasyXp = xpGenerator.generateRandomXp(difficulty = QuestionDifficulty.Easy)
        val easyXpRange = xpGenerator.generatedXpRange * xpGenerator.multiplierFactor(QuestionDifficulty.Easy)
        assertThat(randomEasyXp).isIn(easyXpRange)

        // Test medium difficulty
        val randomMediumXp = xpGenerator.generateRandomXp(difficulty = QuestionDifficulty.Medium)
        val mediumXpRange = xpGenerator.generatedXpRange * xpGenerator.multiplierFactor(QuestionDifficulty.Medium)
        assertThat(randomMediumXp).isIn(mediumXpRange)

        // Test hard difficulty
        val randomHardXp = xpGenerator.generateRandomXp(difficulty = QuestionDifficulty.Hard)
        val hardXpRange = xpGenerator.generatedXpRange * xpGenerator.multiplierFactor(QuestionDifficulty.Hard)
        assertThat(randomHardXp).isIn(hardXpRange)
    }
}