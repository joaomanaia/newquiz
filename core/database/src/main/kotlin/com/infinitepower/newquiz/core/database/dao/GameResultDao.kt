package com.infinitepower.newquiz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity

@Dao
interface GameResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiChoiceResult(result: MultiChoiceGameResultEntity)

    @Query("SELECT * FROM multi_choice_game_results")
    suspend fun getMultiChoiceResults(): List<MultiChoiceGameResultEntity>
}
