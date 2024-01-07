package com.infinitepower.newquiz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.infinitepower.newquiz.core.database.model.MazeQuizItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MazeQuizDao {
    @Query("SELECT * FROM mazeItems")
    suspend fun getAllMazeItems(): List<MazeQuizItemEntity>

    @Query("SELECT * FROM mazeItems")
    fun getAllMazeItemsFlow(): Flow<List<MazeQuizItemEntity>>

    @Query("SELECT * FROM mazeItems WHERE id = :id LIMIT 1")
    suspend fun getMazeItemById(id: Int): MazeQuizItemEntity?

    @Query("SELECT COUNT(id) FROM mazeItems")
    suspend fun countAllItems(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(vararg items: MazeQuizItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<MazeQuizItemEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: MazeQuizItemEntity)

    @Query("DELETE FROM mazeItems")
    suspend fun deleteAll()
}
