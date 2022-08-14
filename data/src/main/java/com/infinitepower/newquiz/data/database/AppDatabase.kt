package com.infinitepower.newquiz.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infinitepower.newquiz.data.database.util.converters.ListConverter
import com.infinitepower.newquiz.data.database.util.converters.LocalDateConverter
import com.infinitepower.newquiz.domain.repository.question.saved_questions.SavedQuestionsDao
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleDao
import com.infinitepower.newquiz.model.question.Question
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem

@TypeConverters(
    ListConverter::class,
    LocalDateConverter::class
)
@Database(
    entities = [Question::class, WordleDailyCalendarItem::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedQuestionsDao(): SavedQuestionsDao

    abstract fun dailyWordleDao(): DailyWordleDao
}