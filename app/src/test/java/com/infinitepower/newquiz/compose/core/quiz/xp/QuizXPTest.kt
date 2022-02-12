package com.infinitepower.newquiz.compose.core.quiz.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.compose.data.local.quiz.QuestionDifficulty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sqrt

internal class QuizXPTest {
    private lateinit var quizXP: QuizXP

    @BeforeEach
    fun setup() {
        quizXP = QuizXP()
    }

    @Test
    fun `getXp by question difficulty`() {
        val easyDifficulty = QuestionDifficulty.Easy
        val easyXp = quizXP.getXp(easyDifficulty)
        assertThat(easyXp).isEqualTo(quizXP.easyXpValue)

        val mediumDifficulty = QuestionDifficulty.Easy
        val mediumXp = quizXP.getXp(mediumDifficulty)
        assertThat(mediumXp).isEqualTo(quizXP.mediumXpValue)

        val hardDifficulty = QuestionDifficulty.Easy
        val hardXp = quizXP.getXp(hardDifficulty)
        assertThat(hardXp).isEqualTo(quizXP.hardXpValue)
    }

    @Test
    fun `get level by xp, test correct level`() {
        assertThat(quizXP.getLevelByXP(-100)).isEqualTo(0)
        assertThat(quizXP.getLevelByXP(0)).isEqualTo(0)
        assertThat(quizXP.getLevelByXP(99)).isEqualTo(0)
        assertThat(quizXP.getLevelByXP(100)).isEqualTo(1)

        for (xp in 0 until 10000 step (10..50).random()) {
            val level = (.1f * sqrt(xp.toDouble())).toInt()
            assertThat(quizXP.getLevelByXP(xp.toLong())).isEqualTo(level)
        }
    }

    @Test
    fun `get level total xp, test correct xp`() {
        assertThat(quizXP.getLevelTotalXP(-100)).isEqualTo(0)
        assertThat(quizXP.getLevelTotalXP(0)).isEqualTo(0)
        assertThat(quizXP.getLevelTotalXP(1)).isEqualTo(100)
        assertThat(quizXP.getLevelTotalXP(2)).isEqualTo(400)
        assertThat(quizXP.getLevelTotalXP(3)).isEqualTo(900)

        for (level in 0 until 100) {
            val levelXP = (level.toDouble().pow(2) * 100).roundToLong()
            assertThat(quizXP.getLevelTotalXP(level)).isEqualTo(levelXP)
        }
    }

    @Test
    fun `get next level xp, test correct next level xp`() {
        assertThat(quizXP.getNextLevelXP(-100)).isEqualTo(0)
        assertThat(quizXP.getNextLevelXP(0)).isEqualTo(100)
        assertThat(quizXP.getNextLevelXP(1)).isEqualTo(400)
        assertThat(quizXP.getNextLevelXP(2)).isEqualTo(900)
        assertThat(quizXP.getNextLevelXP(3)).isEqualTo(1600)

        for (level in 0 until 100) {
            val levelXP = ((level + 1).toDouble().pow(2) * 100).roundToLong()
            assertThat(quizXP.getNextLevelXP(level)).isEqualTo(levelXP)
        }
    }

    @Test
    fun `get required xp to next level, test correct required xp`() {
        assertThat(quizXP.getRequiredXPToNextLevel(-100)).isEqualTo(0)
        assertThat(quizXP.getRequiredXPToNextLevel(0)).isEqualTo(100)
        assertThat(quizXP.getRequiredXPToNextLevel(99)).isEqualTo(1)
        assertThat(quizXP.getRequiredXPToNextLevel(100)).isEqualTo(300)
        assertThat(quizXP.getRequiredXPToNextLevel(353)).isEqualTo(47)

        for (xp in 0 until 10000 step (10..50).random()) {
            val requiredXP = quizXP.getNextLevelXP(quizXP.getLevelByXP(xp.toLong())) - xp
            assertThat(quizXP.getRequiredXPToNextLevel(xp.toLong())).isEqualTo(requiredXP)
        }
    }
}