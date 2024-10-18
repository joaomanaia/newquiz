package com.infinitepower.newquiz.feature.maze.level_results

import com.infinitepower.newquiz.model.maze.MazeQuiz

data class LevelResultsScreenUiState(
    val loading: Boolean = true,
    val completed: Boolean = false,
    val currentItem: MazeQuiz.MazeItem? = null,
    val nextAvailableItem: MazeQuiz.MazeItem? = null,
    val error: String? = null
)
