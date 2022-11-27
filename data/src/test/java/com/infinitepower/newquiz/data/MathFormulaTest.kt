package com.infinitepower.newquiz.data

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.math.evaluator.Expressions
import org.junit.jupiter.api.Test

class MathFormulaTest {
    private val expressions by lazy { Expressions() }

    @Test
    fun a() {
        val expression = "1 + 1 = 2"

        val leftExpression = expression.takeWhile { it != '=' }
        val rightSolution = expression.takeLastWhile { it != '=' }.toDouble()

        val solution = expressions
            .eval(leftExpression)
            .toDouble()

        assertThat(solution).isEqualTo(rightSolution)
    }
}