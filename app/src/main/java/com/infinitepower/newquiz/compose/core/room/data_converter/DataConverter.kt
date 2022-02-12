package com.infinitepower.newquiz.compose.core.room.data_converter

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataConverter {
    @TypeConverter
    fun List<String>.fromStringList(): String = Json.encodeToString(this)

    @TypeConverter
    fun String.toStringList(): List<String> = Json.decodeFromString(this)
}