package com.infinitepower.newquiz.data.database

import android.annotation.SuppressLint
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infinitepower.newquiz.data.database.util.converters.ListConverter
import com.infinitepower.newquiz.data.database.util.converters.LocalDateConverter
import com.infinitepower.newquiz.data.database.util.converters.MathFormulaConverter
import com.infinitepower.newquiz.data.database.util.converters.QuestionDifficultyConverter
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizDao
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsDao
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleDao
import com.infinitepower.newquiz.model.maze.MazeQuizItemEntity
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem

@SuppressLint("all")
@TypeConverters(
    ListConverter::class,
    LocalDateConverter::class,
    QuestionDifficultyConverter::class,
    MathFormulaConverter::class
)
@Database(
    entities = [
        MultiChoiceQuestion::class,
        WordleDailyCalendarItem::class,
        MazeQuizItemEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedQuestionsDao(): SavedMultiChoiceQuestionsDao

    abstract fun dailyWordleDao(): DailyWordleDao

    abstract fun mazeQuizDao(): MazeQuizDao
}