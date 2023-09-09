package com.infinitepower.newquiz.core.database

import android.annotation.SuppressLint
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.infinitepower.newquiz.core.database.dao.DailyChallengeDao
import com.infinitepower.newquiz.core.database.dao.MazeQuizDao
import com.infinitepower.newquiz.core.database.dao.SavedMultiChoiceQuestionsDao
import com.infinitepower.newquiz.core.database.model.DailyChallengeTaskEntity
import com.infinitepower.newquiz.core.database.model.MazeQuizItemEntity
import com.infinitepower.newquiz.core.database.model.MultiChoiceQuestionEntity
import com.infinitepower.newquiz.core.database.util.converters.ListConverter
import com.infinitepower.newquiz.core.database.util.converters.LocalDateConverter
import com.infinitepower.newquiz.core.database.util.converters.MathFormulaConverter
import com.infinitepower.newquiz.core.database.util.converters.QuestionDifficultyConverter

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
        internal const val DATABASE_NAME = "app-database"
    }

    @DeleteTable(tableName = "wordle_daily_calendar")
    class RemoveDailyWordleTableMigration : AutoMigrationSpec
}

