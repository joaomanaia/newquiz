package com.infinitepower.newquiz.core.user_services.domain.xp

import com.infinitepower.newquiz.core.util.kotlin.times
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.random.Random

interface XpGenerator {
    val generatedXpRange: UIntRange

    fun multiplierFactor(
        difficulty: QuestionDifficulty
    ): Float = when (difficulty) {
        is QuestionDifficulty.Easy -> 1f
        is QuestionDifficulty.Medium -> 1.5f
        is QuestionDifficulty.Hard -> 2f
    }

    fun generateRandomXp(
        random: Random = Random
    ): UInt = generatedXpRange.random(random)

    fun generateRandomXp(
        difficulty: QuestionDifficulty,
        random: Random = Random
    ): UInt {
        val randomXp = generateRandomXp(random)

        val multiplierFactor = multiplierFactor(difficulty)
        return randomXp * multiplierFactor
    }
}
