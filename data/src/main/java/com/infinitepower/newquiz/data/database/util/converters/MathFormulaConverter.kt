package com.infinitepower.newquiz.data.database.util.converters

import androidx.room.TypeConverter
import com.infinitepower.newquiz.model.math_quiz.MathFormula

class MathFormulaConverter {
    @TypeConverter
    fun mathFormulaToJson(value: MathFormula): String = value.fullFormulaWithoutSpaces

    @TypeConverter
    fun stringToMathFormula(value: String): MathFormula = MathFormula.fromStringFullFormula(value)
}