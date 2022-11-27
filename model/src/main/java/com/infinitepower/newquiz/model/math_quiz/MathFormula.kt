package com.infinitepower.newquiz.model.math_quiz

data class MathFormula(
    val leftFormula: String,
    val solution: Int
) {
    val fullFormula: String
        get() = "$leftFormula = $solution"

    val fullFormulaWithoutSpaces: String
        get() = fullFormula.replace(" ", "")

    override fun toString(): String = fullFormula
}