package com.infinitepower.newquiz.domain.repository.math_quiz

import com.infinitepower.newquiz.model.math_quiz.MathFormula

interface MathQuizCoreRepository {
    fun generateMathFormula(
        itemSize: Int = 3
    ): MathFormula
}