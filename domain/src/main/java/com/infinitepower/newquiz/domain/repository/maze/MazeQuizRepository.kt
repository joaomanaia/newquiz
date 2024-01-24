package com.infinitepower.newquiz.domain.repository.maze

import com.infinitepower.newquiz.model.maze.MazeQuiz
import kotlinx.coroutines.flow.Flow

interface MazeQuizRepository {
    fun getSavedMazeQuizFlow(): Flow<MazeQuiz>

    suspend fun countAllItems(): Int

    suspend fun insertItems(items: List<MazeQuiz.MazeItem>)
}
