package com.infinitepower.newquiz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinitepower.newquiz.core.database.model.user.ComparisonQuizGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.WordleGameResultEntity

@Dao
interface GameResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiChoiceResult(result: MultiChoiceGameResultEntity)

    @Query("SELECT * FROM multi_choice_game_results")
    suspend fun getMultiChoiceResults(): List<MultiChoiceGameResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordleResult(result: WordleGameResultEntity)

    @Query("SELECT * FROM wordle_game_results")
    suspend fun getWordleResults(): List<WordleGameResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComparisonQuizResult(result: ComparisonQuizGameResultEntity)

    @Query("SELECT * FROM comparison_quiz_game_results")
    suspend fun getComparisonQuizResults(): List<ComparisonQuizGameResultEntity>
}
