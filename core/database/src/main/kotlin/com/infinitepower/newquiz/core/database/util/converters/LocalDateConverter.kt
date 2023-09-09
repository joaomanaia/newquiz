package com.infinitepower.newquiz.core.database.util.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun localDateToJson(value: LocalDate): String = value.toString()

    @TypeConverter
    fun stringToLocalDate(value: String): LocalDate = LocalDate.parse(value)
}