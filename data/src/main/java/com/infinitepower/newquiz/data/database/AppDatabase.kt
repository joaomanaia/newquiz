package com.infinitepower.newquiz.data.database

import android.annotation.SuppressLint
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.infinitepower.newquiz.data.database.util.converters.ListConverter
import com.infinitepower.newquiz.data.database.util.converters.LocalDateConverter
import com.infinitepower.newquiz.data.database.util.converters.MathFormulaConverter
import com.infinitepower.newquiz.data.database.util.converters.QuestionDifficultyConverter
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeDao
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizDao
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsDao
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTaskEntity
import com.infinitepower.newquiz.model.maze.MazeQuizItemEntity
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionEntity

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
        MazeQuizItemEntity::class,
        DailyChallengeTaskEntity::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(
            from = 2,
            to = 3,
            spec = AppDatabase.RemoveDailyWordleTableMigration::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedQuestionsDao(): SavedMultiChoiceQuestionsDao

    abstract fun mazeQuizDao(): MazeQuizDao

    abstract fun dailyChallengeDao(): DailyChallengeDao

    companion object {
        const val DATABASE_NAME = "app-database"
    }

    @DeleteTable(tableName = "wordle_daily_calendar")
    class RemoveDailyWordleTableMigration : AutoMigrationSpec
}

