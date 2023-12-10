package com.infinitepower.newquiz.core.database.model.user

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(
    tableName = "multi_choice_game_results"
)
data class MultiChoiceGameResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    override val gameId: Int = 0,
    @ColumnInfo(name = "correct_answers")
    val correctAnswers: Int,
    @ColumnInfo(name = "skipped_questions")
    val skippedQuestions: Int,
    @ColumnInfo(name = "question_count")
    val questionCount: Int,
    @ColumnInfo(name = "average_answer_time")
    val averageAnswerTime: Double,
    @ColumnInfo(name = "earned_xp")
    override val earnedXp: Int,
    @ColumnInfo(name = "played_at")
    override val playedAt: Long,
) : BaseGameResultEntity
