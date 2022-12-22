package com.infinitepower.newquiz.domain.repository.math_quiz.maze

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.infinitepower.newquiz.model.math_quiz.maze.MathQuizMaze
import kotlinx.coroutines.flow.Flow

@Dao
interface MazeQuizDao {
    @Query("SELECT * FROM mazeItems")
    suspend fun getAllMazeItems(): List<MathQuizMaze.MazeItem>

    @Query("SELECT * FROM mazeItems")
    fun getAllMazeItemsFlow(): Flow<List<MathQuizMaze.MazeItem>>

    @Query("SELECT * FROM mazeItems WHERE played == 0  ORDER BY id ASC LIMIT 1")
    suspend fun getCurrentPlayItem(): MathQuizMaze.MazeItem?

    @Query("SELECT * FROM mazeItems WHERE id = :id LIMIT 1")
    suspend fun getMazeItemById(id: Int): MathQuizMaze.MazeItem?

    @Query("SELECT COUNT(id) FROM mazeItems")
    suspend fun countAllItems(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(vararg items: MathQuizMaze.MazeItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<MathQuizMaze.MazeItem>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(item: MathQuizMaze.MazeItem)

    @Query("DELETE FROM mazeItems")
    suspend fun deleteAll()
}