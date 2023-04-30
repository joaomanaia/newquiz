package com.infinitepower.newquiz.data.database

import android.annotation.SuppressLint
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.infinitepower.newquiz.data.database.util.converters.ListConverter
import com.infinitepower.newquiz.data.database.util.converters.LocalDateConverter
import com.infinitepower.newquiz.data.database.util.converters.MathFormulaConverter
import com.infinitepower.newquiz.data.database.util.converters.QuestionDifficultyConverter
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeDao
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizDao
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsDao
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleDao
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTaskEntity
import com.infinitepower.newquiz.model.maze.MazeQuizItemEntity
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionEntity
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
        MultiChoiceQuestionEntity::class,
        WordleDailyCalendarItem::class,
        MazeQuizItemEntity::class,
        DailyChallengeTaskEntity::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedQuestionsDao(): SavedMultiChoiceQuestionsDao

    abstract fun dailyWordleDao(): DailyWordleDao

    abstract fun mazeQuizDao(): MazeQuizDao

    abstract fun dailyChallengeDao(): DailyChallengeDao
}