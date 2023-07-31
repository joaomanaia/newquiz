package com.infinitepower.newquiz.data.local.math_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.math.evaluator.Expressions
import com.infinitepower.newquiz.data.repository.math_quiz.MathQuizCoreRepositoryImpl
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

internal class MathQuizCoreRepositoryImplTest {

    private lateinit var mathQuizCoreRepository: MathQuizCoreRepositoryImpl

    companion object {
        @JvmStatic
        private fun getGenerateMathFormulaParams() = listOf(
            Arguments.of(1, QuestionDifficulty.Easy),
            Arguments.of(1, QuestionDifficulty.Medium),
            Arguments.of(1, QuestionDifficulty.Hard),
            Arguments.of(2, QuestionDifficulty.Easy),
            Arguments.of(2, QuestionDifficulty.Medium),
            Arguments.of(2, QuestionDifficulty.Hard),
            Arguments.of(3, QuestionDifficulty.Easy),
            Arguments.of(3, QuestionDifficulty.Medium),
            Arguments.of(3, QuestionDifficulty.Hard)
        )
    }

    @BeforeEach
    fun setup() {
        val expressions = Expressions()
        mathQuizCoreRepository = MathQuizCoreRepositoryImpl(expressions)
    }

    @ParameterizedTest(name = "generate math formula, operator size: {0}, difficulty: {1}")
    @MethodSource("getGenerateMathFormulaParams")
    fun `generate math formula`(
        operatorSize: Int,
        difficulty: QuestionDifficulty
    ) {
        val timedValue = measureTimedValue {
            mathQuizCoreRepository.generateMathFormula(operatorSize, difficulty)
        }

        println("Time: ${timedValue.duration}")

        val formula = timedValue.value
        println("Formula: $formula")

        assertThat(formula.fullFormula).matches(MathFormula.mathFormulaRegex.toPattern())
        assertThat(formula.solution).isIn(MathQuizCoreRepository.SOLUTION_RANGE)

        val solution = Expressions().eval(formula.leftFormula).toInt()
        assertThat(solution).isEqualTo(formula.solution)
    }

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
            mathQuizCoreRepository.validateFormula("2+2=4")
        }.also {
            println(it)
        }
    }
}