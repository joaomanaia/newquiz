package com.infinitepower.newquiz.data.database

import android.annotation.SuppressLint
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infinitepower.newquiz.data.database.util.converters.ListConverter
import com.infinitepower.newquiz.data.database.util.converters.LocalDateConverter
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsDao
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleDao
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem

@SuppressLint("all")
@TypeConverters(
    ListConverter::class,
    LocalDateConverter::class
)
@Database(
    entities = [MultiChoiceQuestion::class, WordleDailyCalendarItem::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedQuestionsDao(): SavedMultiChoiceQuestionsDao

    abstract fun dailyWordleDao(): DailyWordleDao
}