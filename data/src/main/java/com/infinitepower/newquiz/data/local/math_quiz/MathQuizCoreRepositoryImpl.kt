package com.infinitepower.newquiz.data.local.math_quiz

import com.infinitepower.newquiz.core.math.MathOperator
import com.infinitepower.newquiz.core.math.evaluator.Expressions
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import java.lang.ArithmeticException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt
import kotlin.random.Random

@Singleton
class MathQuizCoreRepositoryImpl @Inject constructor(
    private val expressions: Expressions
) : MathQuizCoreRepository {
    override suspend fun generateMathFormula(
        operatorSize: Int,
        answerRange: IntRange,
        difficulty: QuestionDifficulty,
        random: Random
    ): MathFormula {
        val formula = mutableListOf<Char>()

        formula.addAll(randomNumbers(answerRange, difficulty, random))

        repeat(operatorSize) {
            formula.add(MathOperator.randomOperatorByDifficulty(difficulty, random).value)

            formula.addAll(randomNumbers(answerRange, difficulty, random))
        }

        val leftFormula = formula.joinToString("") { c ->
            if (c.isDigit()) c.toString() else " $c "
        }

        val solution = try {
            Expressions()
                .eval(leftFormula)
                .toDouble()
                .roundToInt()
        } catch (e: ArithmeticException) {
            return generateMathFormula(
                operatorSize,
                answerRange,
                difficulty,
                random
            )
        }

        if (solution !in -1000..1000)
            return generateMathFormula(
                operatorSize,
                answerRange,
                difficulty,
                random
            )

        return MathFormula(leftFormula, solution)
    }

    override fun validateFormula(formula: String): Boolean {
        // Checks if formula contains one equals
        if (formula.count { it == '=' } != 1) return false

        val leftExpression = formula.takeWhile { it != '=' }
        val rightSolution = formula.takeLastWhile { it != '=' }.toDouble()

        val solution = expressions
            .eval(leftExpression)
            .toDouble()

        return solution == rightSolution
    }

    private fun randomNumbers(
        answerRange: IntRange,
        difficulty: QuestionDifficulty,
        random: Random = Random
    ): List<Char> {
        val twoDigits = random.nextFloat() < difficulty.probTwoDigitNumber
        val digitLength = if (twoDigits) 2 else 1

        return List(digitLength) {
            answerRange.random(random).digitToChar()
        }
    }

    private val QuestionDifficulty.probTwoDigitNumber: Float
        get() = when (this) {
            QuestionDifficulty.Easy -> 0f
            QuestionDifficulty.Medium -> 0.5f
            QuestionDifficulty.Hard -> 1f
        }
}