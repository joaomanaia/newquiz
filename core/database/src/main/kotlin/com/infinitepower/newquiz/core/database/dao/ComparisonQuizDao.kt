package com.infinitepower.newquiz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.infinitepower.newquiz.core.database.model.ComparisonQuizHighestPosition
import kotlinx.coroutines.flow.Flow

@Dao
interface ComparisonQuizDao {
    @Query("SELECT * FROM comparison_quiz_highest_position WHERE categoryId = :categoryId LIMIT 1")
    suspend fun getHighestPosition(categoryId: String): ComparisonQuizHighestPosition?

    @Query("SELECT * FROM comparison_quiz_highest_position WHERE categoryId = :categoryId LIMIT 1")
    fun getHighestPositionFlow(
        categoryId: String
    ): Flow<ComparisonQuizHighestPosition?>

    @Upsert
    suspend fun upsert(vararg highestPosition: ComparisonQuizHighestPosition)
}