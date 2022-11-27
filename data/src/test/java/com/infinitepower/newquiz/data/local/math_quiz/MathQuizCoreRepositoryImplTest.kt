package com.infinitepower.newquiz.data.local.math_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.math.evaluator.Expressions
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MathQuizCoreRepositoryImplTest {

    private lateinit var mathQuizCoreRepository: MathQuizCoreRepository

    @BeforeEach
    fun setup() {
        val expressions = Expressions()
        mathQuizCoreRepository = MathQuizCoreRepositoryImpl(expressions)
    }

    @Test
    fun `validate correct formula, returns true`() {
        val correctFormula = "1 + 1 = 2"

        val valid = mathQuizCoreRepository.validateFormula(correctFormula)
        assertThat(valid).isTrue()
    }

    @Test
    fun `validate incorrect formula, returns false`() {
        val correctFormula = "1 + 2 = 2"

        val valid = mathQuizCoreRepository.validateFormula(correctFormula)
        assertThat(valid).isFalse()
    }

    @Test
    fun `validate formula without equals, returns false`() {
        val correctFormula = "1 + 2 2"

        val valid = mathQuizCoreRepository.validateFormula(correctFormula)
        assertThat(valid).isFalse()
    }
}