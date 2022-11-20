package com.infinitepower.newquiz.model.math_quiz

@JvmInline
value class MathFormula(val items: List<MathItem>) {
    companion object {
        fun randomMathFormula(): MathFormula {
            val formula = mutableListOf<MathItem>()

            val a = MathItem.randomNumber()
            val b = MathItem.randomNumber()

            return MathFormula(listOf(a, MathItem('+'), b))

            /*
            val numOperators = 1

            for (i in 0..3) {
                if (i % 2 == 0) {

                }
            }
            */
        }
    }
}

@JvmInline
value class MathItem(val value: Char) {
    companion object {
        private val allNumbers = '0'..'9'

        private const val allOperators = "+-*/"

        private const val equalsItem = '='

        fun randomNumber(): MathItem = MathItem(allNumbers.random())

        fun randomOperator(): MathItem = MathItem(allOperators.random())
    }
}