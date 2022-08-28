package com.infinitepower.newquiz.data.database.util.converters

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ListConverter {
    @TypeConverter
    fun stringListToJson(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun jsonToStringList(value: String): List<String> = Json.decodeFromString(value)
}