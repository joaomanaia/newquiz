package com.infinitepower.newquiz.domain.repository.maze

import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.maze.MazeQuiz

interface MazeQuizRepository {
    fun getSavedMazeQuizFlow(): FlowResource<MazeQuiz>

    suspend fun countAllItems(): Int

    suspend fun insertItems(items: List<MazeQuiz.MazeItem>)
}