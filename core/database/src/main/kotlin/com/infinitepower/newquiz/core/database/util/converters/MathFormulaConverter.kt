package com.infinitepower.newquiz.core.database.util.converters

import androidx.room.TypeConverter
import com.infinitepower.newquiz.model.math_quiz.MathFormula

class MathFormulaConverter {
    @TypeConverter
    fun mathFormulaToJson(value: MathFormula): String = value.fullFormula

    @TypeConverter
    fun stringToMathFormula(value: String): MathFormula = MathFormula.fromStringFullFormula(value)
}
