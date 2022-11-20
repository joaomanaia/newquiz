package com.infinitepower.newquiz.data.local.math_quiz

import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizCoreRepository
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MathQuizCoreRepositoryImpl @Inject constructor() : MathQuizCoreRepository {
    override fun generateMathFormula(itemSize: Int): MathFormula {
        TODO("Not yet implemented")
    }
}