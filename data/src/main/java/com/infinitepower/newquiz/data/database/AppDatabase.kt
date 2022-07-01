package com.infinitepower.newquiz.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infinitepower.newquiz.data.database.util.converters.ListConverter
import com.infinitepower.newquiz.domain.repository.question.saved_questions.SavedQuestionsDao
import com.infinitepower.newquiz.model.question.Question

@TypeConverters(ListConverter::class)
@Database(entities = [Question::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedQuestionsDao(): SavedQuestionsDao
}