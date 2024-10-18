package com.infinitepower.newquiz.core.navigation

import com.infinitepower.newquiz.model.maze.MazeQuiz

interface MazeNavigator {
    fun navigateToGame(item: MazeQuiz.MazeItem)

    fun navigateToMazeResults(itemId: Int)
}
