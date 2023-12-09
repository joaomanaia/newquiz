package com.infinitepower.newquiz.core.database.dao

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinitepower.newquiz.core.database.model.user.ComparisonQuizGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.WordleGameResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiChoiceResult(vararg result: MultiChoiceGameResultEntity)

    @Query("SELECT * FROM multi_choice_game_results")
    suspend fun getMultiChoiceResults(): List<MultiChoiceGameResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordleResult(vararg result: WordleGameResultEntity)

    @Query("SELECT * FROM wordle_game_results")
    suspend fun getWordleResults(): List<WordleGameResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComparisonQuizResult(vararg result: ComparisonQuizGameResultEntity)

    @Query("SELECT * FROM comparison_quiz_game_results")
    suspend fun getComparisonQuizResults(): List<ComparisonQuizGameResultEntity>

    @Keep
    data class XpForPlayedAt(
        @ColumnInfo(name = "earned_xp")
        val earnedXp: Int,
        @ColumnInfo(name = "played_at")
        val playedAt: Long,
    )

    @Query("""
        SELECT earned_xp, played_at FROM multi_choice_game_results
        WHERE played_at BETWEEN :startDate AND :endDate
        UNION ALL
        SELECT earned_xp, played_at FROM wordle_game_results
        WHERE played_at BETWEEN :startDate AND :endDate
        UNION ALL
        SELECT earned_xp, played_at FROM comparison_quiz_game_results
        WHERE played_at BETWEEN :startDate AND :endDate
    """)
    suspend fun getXpForDateRange(
        startDate: Long,
        endDate: Long
    ): List<XpForPlayedAt>

    @Query("""
        SELECT earned_xp, played_at FROM multi_choice_game_results
        WHERE played_at BETWEEN :startDate AND :endDate
        UNION ALL
        SELECT earned_xp, played_at FROM wordle_game_results
        WHERE played_at BETWEEN :startDate AND :endDate
        UNION ALL
        SELECT earned_xp, played_at FROM comparison_quiz_game_results
        WHERE played_at BETWEEN :startDate AND :endDate
    """)
    fun getXpForDateRangeFlow(
        startDate: Long,
        endDate: Long
    ): Flow<List<XpForPlayedAt>>
}
