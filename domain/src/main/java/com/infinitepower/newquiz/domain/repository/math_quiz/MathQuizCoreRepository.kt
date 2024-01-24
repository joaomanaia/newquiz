package com.infinitepower.newquiz.domain.repository.math_quiz

import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.random.Random

interface MathQuizCoreRepository {
    companion object {
        /**
         * The range of numbers that can be generated for the solution of the formula.
         */
        val SOLUTION_RANGE = -999..999

        const val MAX_FORMULA_LENGTH = 10

        /**
         * The range of numbers that can be generated for each number of the formula.
         */
        val QuestionDifficulty.numbers
            get() = when (this) {
                QuestionDifficulty.Easy -> 0..9
                QuestionDifficulty.Medium -> 0..49
                QuestionDifficulty.Hard -> 0..99
            }

        /**
         * The operators that can be generated for each difficulty.
         */
        val QuestionDifficulty.operators
            get() = when (this) {
                QuestionDifficulty.Easy -> listOf('+', '-')
                QuestionDifficulty.Medium -> listOf('+', '-', '*')
                QuestionDifficulty.Hard -> listOf('+', '-', '*', '/')
            }

        val QuestionDifficulty.operatorSizeRange
            get() = when (this) {
                QuestionDifficulty.Easy -> 1..1
                QuestionDifficulty.Medium -> 1..2
                QuestionDifficulty.Hard -> 1..3
            }
    }

    fun generateMathFormula(
        operatorSize: Int = 1,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): MathFormula

    fun validateFormula(formula: String): Boolean
}
