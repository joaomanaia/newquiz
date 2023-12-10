package com.infinitepower.newquiz.core.database.model.user

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "comparison_quiz_game_results")
data class ComparisonQuizGameResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    override val gameId: Int = 0,
    @ColumnInfo(name = "earned_xp")
    override val earnedXp: Int,
    @ColumnInfo(name = "played_at")
    override val playedAt: Long,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
    @ColumnInfo(name = "comparison_mode")
    val comparisonMode: String,
    @ColumnInfo(name = "end_position")
    val endPosition: Int,
    @ColumnInfo(name = "highest_position")
    val highestPosition: Int,
    @ColumnInfo(name = "skipped_answers")
    val skippedAnswers: Int
) : BaseGameResultEntity
