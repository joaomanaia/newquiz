package com.infinitepower.newquiz.core.database.model.user

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(
    tableName = "wordle_game_results"
)
data class WordleGameResultEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "game_id")
    override val gameId: Int = 0,
    @ColumnInfo(name = "earned_xp")
    override val earnedXp: Int,
    @ColumnInfo(name = "played_at")
    override val playedAt: Long,
    @ColumnInfo(name = "word_length")
    val wordLength: Int,
    @ColumnInfo(name = "rows_used")
    val rowsUsed: Int,
    @ColumnInfo(name = "max_rows")
    val maxRows: Int,
    @ColumnInfo(name = "category_id")
    val categoryId: String,
) : BaseGameResultEntity
