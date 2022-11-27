package com.infinitepower.newquiz.core.math

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class MathFormulaGeneratorTest {
    @Test
    fun `generate operator when easy and one operator`() {
        val random = Random

        val formula = generateMathFormula(
            operatorSize = 1,
            difficulty = QuestionDifficulty.Easy,
            random = random
        )

        assertThat(formula.solution).isIn(-1000..1000)

        val firstNumbers = formula
            .leftFormula
            .takeWhile(Char::isDigit)
            .toInt()

        println(formula.toString())

        assertThat(firstNumbers).isIn(0..9)
        assertThat(formula.leftFormula).hasLength(5)
    }

    @Test
    fun `generate operator when medium and one operator`() {
        val random = Random

        val formula = generateMathFormula(
            operatorSize = 1,
            difficulty = QuestionDifficulty.Medium,
            random = random
        )

        assertThat(formula.solution).isIn(-1000..1000)

        val firstNumbers = formula
            .leftFormula
            .takeWhile(Char::isDigit)
            .toInt()

        println(formula.toString())

        assertThat(firstNumbers).isIn(0..99)
    }

    @Test
    fun `generate operator when hard and two operator`() {
        val random = Random

        val formula = generateMathFormula(
            operatorSize = 2,
            difficulty = QuestionDifficulty.Hard,
            random = random
        )

        assertThat(formula.solution).isIn(-1000..1000)

        val firstNumbers = formula
            .leftFormula
            .takeWhile(Char::isDigit)
            .toInt()

        println(formula.toString())

        assertThat(firstNumbers).isIn(0..99)
    }
}