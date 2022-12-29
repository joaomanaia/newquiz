package com.infinitepower.newquiz.data.local.math_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.math.evaluator.Expressions
import com.infinitepower.newquiz.data.repository.math_quiz.MathQuizCoreRepositoryImpl
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

internal class MathQuizCoreRepositoryImplTest {

    private lateinit var mathQuizCoreRepository: MathQuizCoreRepositoryImpl

    @BeforeEach
    fun setup() {
        val expressions = Expressions()
        mathQuizCoreRepository = MathQuizCoreRepositoryImpl(expressions)
    }

    @Test
    fun `generate math formula, with one operator size and easy difficulty`() {
        val random = Random(0)

        val operatorSize = 1
        val difficulty = QuestionDifficulty.Easy

        val formula = mathQuizCoreRepository.generateMathFormula(operatorSize, difficulty, random)

        assertThat(formula.fullFormulaWithoutSpaces).matches(MathFormula.mathFormulaRegex.toPattern())
        assertThat(formula.solution).isIn(0..99)

        val solution = Expressions().eval(formula.leftFormula).toInt()
        assertThat(solution).isEqualTo(formula.solution)
    }

    @Test
    fun `generate math formula, with one operator size and medium difficulty`() {
        val random = Random(0)

        val operatorSize = 1
        val difficulty = QuestionDifficulty.Medium

        val formula = mathQuizCoreRepository.generateMathFormula(operatorSize, difficulty, random)

        assertThat(formula.fullFormulaWithoutSpaces).matches(MathFormula.mathFormulaRegex.toPattern())
        assertThat(formula.solution).isIn(0..99)

        val solution = Expressions().eval(formula.leftFormula).toInt()
        assertThat(solution).isEqualTo(formula.solution)
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `validate correct formulas`() {
        // Test formulas with not valid operators
        assertThat(mathQuizCoreRepository.validateFormula("2+2s=4")).isFalse()

        // Test valid formulas
        assertThat(mathQuizCoreRepository.validateFormula("2+2=4")).isTrue()
        assertThat(mathQuizCoreRepository.validateFormula("2*3=6")).isTrue()
        assertThat(mathQuizCoreRepository.validateFormula("10/5=2")).isTrue()

        // Test formulas with multiple equals signs
        assertThat(mathQuizCoreRepository.validateFormula("2+2=4=")).isFalse()
        assertThat(mathQuizCoreRepository.validateFormula("2+2=4=8")).isFalse()

        // Test formulas with empty left-hand side expression
        assertThat(mathQuizCoreRepository.validateFormula("=4")).isFalse()
        assertThat(mathQuizCoreRepository.validateFormula("  =  4  ")).isFalse()

        // Test formulas with empty right-hand side solution
        assertThat(mathQuizCoreRepository.validateFormula("2+2=")).isFalse()
        assertThat(mathQuizCoreRepository.validateFormula("2+2=  ")).isFalse()

        // Test formulas with invalid right-hand side solution
        assertThat(mathQuizCoreRepository.validateFormula("2+2=abc")).isFalse()
        assertThat(mathQuizCoreRepository.validateFormula("2+2=1.2.3")).isFalse()

        // Test formulas with invalid left-hand side expression
        assertThat(mathQuizCoreRepository.validateFormula("2+a=4")).isFalse()

        measureTime {
            repeat(10000) {
                mathQuizCoreRepository.validateFormula("2+2=4")
            }
        }.also {
            println(it)
        }
    }

    @Test
    fun `random number generator, with easy difficulty`() {
        // Create a random number generator with a fixed seed to ensure
        // reproducible test results
        val random = Random(0)

        val difficulty = QuestionDifficulty.Easy
        val result = mathQuizCoreRepository.randomNumbers(difficulty, random)
        assertThat(result).containsExactly('8')
    }

    @Test
    fun `random number generator, with medium difficulty and random seed 0`() {
        // Create a random number generator with a fixed seed to ensure
        // reproducible test results
        val random = Random(0)

        val difficulty = QuestionDifficulty.Medium
        val result = mathQuizCoreRepository.randomNumbers(difficulty, random)
        assertThat(result).containsExactly('8')
    }

    @Test
    fun `random number generator, with medium difficulty and random seed 1`() {
        // Create a random number generator with a fixed seed to ensure
        // reproducible test results
        val random = Random(1)

        val difficulty = QuestionDifficulty.Medium
        val result = mathQuizCoreRepository.randomNumbers(difficulty, random)
        assertThat(result).containsExactly('6', '2')
    }

    @Test
    fun `random number generator, with hard difficulty`() {
        // Create a random number generator with a fixed seed to ensure
        // reproducible test results
        val random = Random(0)

        val difficulty = QuestionDifficulty.Hard
        val result = mathQuizCoreRepository.randomNumbers(difficulty, random)
        assertThat(result).containsExactly('8', '7')
    }
}