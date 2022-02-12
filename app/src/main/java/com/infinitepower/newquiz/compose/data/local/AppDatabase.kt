package com.infinitepower.newquiz.compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infinitepower.newquiz.compose.core.room.data_converter.DataConverter
import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.data.local.question.SavedQuestionDao

@Database(
    entities = [Question::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedQuestionDao(): SavedQuestionDao
}