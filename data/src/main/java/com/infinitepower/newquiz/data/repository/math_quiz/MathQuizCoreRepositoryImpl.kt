package com.infinitepower.newquiz.data.repository.math_quiz

import android.os.Build
import com.infinitepower.newquiz.core.math.evaluator.Expressions
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository.Companion.numbers
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository.Companion.operators
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MathQuizCoreRepositoryImpl @Inject constructor(
    private val expressions: Expressions
) : MathQuizCoreRepository {
    private val allNumbers by lazy { 0..9 }
    private val allOperators by lazy { "+-*/=" }

    override fun generateMathFormula(
        operatorSize: Int,
        difficulty: QuestionDifficulty,
        random: Random
    ): MathFormula {
        val formula = StringBuilder()
        var operatorCount = 0

        // Add the first number
        formula.append(getRandomNumber(difficulty, random))

        while (operatorCount < operatorSize && formula.length < MathQuizCoreRepository.MAX_FORMULA_LENGTH) {
            // Get the operator
            val operator = getRandomOperator(difficulty, random)

            // Get the number
            val number = getRandomNumber(difficulty, random)

            if (operator == '/' && number == 0) {
                // Skip division by zero
                continue
            }

            val newFormula = StringBuilder(formula).apply {
                append(operator)
                append(number)
            }

            // Check if the formula is in range
            if (getSolution(newFormula.toString()) !in MathQuizCoreRepository.SOLUTION_RANGE) {
                // Skip if the formula is not in range

                continue
            }

            formula.append(operator)
            formula.append(number)

            operatorCount++
        }

        return MathFormula(formula.toString(), getSolution(formula.toString()))
    }

    private fun getRandomNumber(
        difficulty: QuestionDifficulty,
        random: Random = Random
    ): Int = difficulty.numbers.random(random)

    private fun getRandomOperator(
        difficulty: QuestionDifficulty,
        random: Random = Random
    ): Char = difficulty.operators.random(random)

    private fun getSolution(formula: String): Int {
        val solutionBigInt = expressions.eval(formula).toBigInteger()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            solutionBigInt.intValueExact()
        } else {
            solutionBigInt.toInt()
        }
    }

    override fun validateFormula(formula: String): Boolean {
        val allNumbersStr = allNumbers.map(Int::digitToChar)
        val allItemsNumberOrOperator = formula.all {
            it in allNumbersStr || it in allOperators
        }

        if (!allItemsNumberOrOperator) return false

        // Checks if formula contains one equals
        if (formula.count { it == '=' } != 1) return false

        val leftExpression = formula.takeWhile { it != '=' }
        val rightSolution = formula
            .takeLastWhile { it != '=' }
            .toDoubleOrNull() ?: return false

        // Evaluate left-hand side expression and compare it with right-hand side solution
        return try {
            val solution = expressions.eval(leftExpression).toDouble()
            solution == rightSolution
        } catch (e: Exception) {
            false
        }
    }
}