package com.infinitepower.newquiz.data.database.util.converters

import androidx.room.TypeConverter
import com.infinitepower.newquiz.model.math_quiz.MathFormula

class MathFormulaConverter {
    @TypeConverter
    fun localDateToJson(value: MathFormula): String = value.fullFormulaWithoutSpaces

    @TypeConverter
    fun stringToLocalDate(value: String): MathFormula = MathFormula.fromStringFullFormula(value)
}