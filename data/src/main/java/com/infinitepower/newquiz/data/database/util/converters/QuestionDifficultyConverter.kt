package com.infinitepower.newquiz.data.database.util.converters

import androidx.room.TypeConverter
import com.infinitepower.newquiz.model.question.QuestionDifficulty

class QuestionDifficultyConverter {
    @TypeConverter
    fun localDateToJson(value: QuestionDifficulty): String = value.id

    @TypeConverter
    fun stringToLocalDate(value: String): QuestionDifficulty = QuestionDifficulty.from(value)
}