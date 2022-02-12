package com.infinitepower.newquiz.compose.core.quiz.xp

import androidx.annotation.VisibleForTesting
import com.infinitepower.newquiz.compose.data.local.quiz.QuestionDifficulty
import kotlin.math.pow
import kotlin.math.roundToLong
import kotlin.math.sqrt

open class QuizXP {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val easyXpValue = 30L

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val mediumXpValue = 50L

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val hardXpValue = 100L

    /**
     * Gets xp value by [QuestionDifficulty]
     *
     * @param difficulty provide by question
     */
    fun getXp(difficulty: QuestionDifficulty) = when(difficulty) {
        is QuestionDifficulty.Easy -> easyXpValue
        is QuestionDifficulty.Medium -> mediumXpValue
        is QuestionDifficulty.Hard -> hardXpValue
    }

    fun getBonusAllQuestionsCorrectXp() = (easyXpValue..hardXpValue).random()

    /**
     * Gets level by xp input
     *
     * @param xp to be calculated to level by 0.1 * sqrt(xp)
     */
    fun getLevelByXP(xp: Long) = if (xp < 0) 0 else (.1 * sqrt(xp.toDouble())).toInt()

    fun getLevelTotalXP(level: Int) = if (level < 0) 0L else (level.toDouble().pow(2) * 100).roundToLong()

    fun getNextLevelXP(
        currentLevel: Int
    ) = if (currentLevel < 0) 0L else ((currentLevel + 1).toDouble().pow(2) * 100).roundToLong()

    fun getRequiredXPToNextLevel(
        currentXP: Long
    ) = if (currentXP < 0) 0L else getNextLevelXP(getLevelByXP(currentXP)) - currentXP
}