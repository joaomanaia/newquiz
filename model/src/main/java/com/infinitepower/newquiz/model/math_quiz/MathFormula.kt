package com.infinitepower.newquiz.model.math_quiz

import androidx.annotation.Keep

@Keep
data class MathFormula(
    val leftFormula: String,
    val solution: Int
) {
    private val fullFormula: String
        get() = "$leftFormula = $solution"

    val fullFormulaWithoutSpaces: String
        get() = fullFormula.replace(" ", "")

    val operatorSize: Int
        get() = leftFormula.count { it in "+-*/" }

    override fun toString(): String = fullFormula

    companion object {
        val mathFormulaRegex = "(\\d+(\\+|\\-|\\*|\\/))+\\d+\\=\\d+".toRegex()

        fun fromStringFullFormula(value: String): MathFormula = MathFormula(
            leftFormula = value.takeWhile { it != '=' },
            solution = value
                .takeLastWhile { it != '=' }
                .toIntOrNull() ?: throw IllegalArgumentException()
        )
    }
}