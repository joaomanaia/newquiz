package com.infinitepower.newquiz.core.math

import com.infinitepower.newquiz.core.util.kotlin.increaseEndBy
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.random.Random

interface MathOperatorUtil {
    fun getOperatorProbability(difficulty: QuestionDifficulty): Float
}

sealed class MathOperator(val value: Char) : MathOperatorUtil {
    companion object {
        fun randomOperatorByDifficulty(
            difficulty: QuestionDifficulty,
            random: Random = Random
        ): MathOperator {
            val pAddiction = Addiction.getOperatorProbability(difficulty)
            val pAddictionRange = 0f..pAddiction

            val pSubtraction = Subtraction.getOperatorProbability(difficulty)
            val pSubtractionRange = pAddictionRange increaseEndBy pSubtraction

            val pMultiplication = Multiplication.getOperatorProbability(difficulty)
            val pMultiplicationRange = pSubtractionRange increaseEndBy pMultiplication

            return when (random.nextFloat()) {
                in pAddictionRange -> Addiction
                in pSubtractionRange -> Subtraction
                in pMultiplicationRange -> Multiplication
                else -> Division
            }
        }
    }

    object Addiction : MathOperator('+') {
        override fun getOperatorProbability(difficulty: QuestionDifficulty): Float {
            return when (difficulty) {
                QuestionDifficulty.Easy -> 0.45f
                QuestionDifficulty.Medium -> 0.35f
                QuestionDifficulty.Hard -> 0.30f
            }
        }
    }

    object Subtraction : MathOperator('-') {
        override fun getOperatorProbability(difficulty: QuestionDifficulty): Float {
            return when (difficulty) {
                QuestionDifficulty.Easy -> 0.45f
                QuestionDifficulty.Medium -> 0.35f
                QuestionDifficulty.Hard -> 0.30f
            }
        }
    }

    object Multiplication : MathOperator('*') {
        override fun getOperatorProbability(difficulty: QuestionDifficulty): Float {
            return when (difficulty) {
                QuestionDifficulty.Easy -> 0.10f
                QuestionDifficulty.Medium -> 0.20f
                QuestionDifficulty.Hard -> 0.20f
            }
        }
    }

    object Division : MathOperator('/') {
        override fun getOperatorProbability(difficulty: QuestionDifficulty): Float {
            return when (difficulty) {
                QuestionDifficulty.Easy -> 0f
                QuestionDifficulty.Medium -> 0.10f
                QuestionDifficulty.Hard -> 0.20f
            }
        }
    }
}