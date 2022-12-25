package com.infinitepower.newquiz.domain.repository.math_quiz

import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.random.Random

interface MathQuizCoreRepository {
    fun generateMathFormula(
        operatorSize: Int = 1,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): MathFormula

    fun validateFormula(formula: String): Boolean
}