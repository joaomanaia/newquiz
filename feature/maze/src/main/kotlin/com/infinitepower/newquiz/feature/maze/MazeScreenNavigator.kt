package com.infinitepower.newquiz.feature.maze

import com.infinitepower.newquiz.model.maze.MazeQuiz

interface MazeScreenNavigator {
    fun navigateToGame(item: MazeQuiz.MazeItem)
}
