package com.infinitepower.newquiz.model.math_quiz

import androidx.annotation.Keep

sealed class MathQuiz {
    @Keep
    data class HiddenItemLocator(
        val mathFormula: MathFormula
    )
}
