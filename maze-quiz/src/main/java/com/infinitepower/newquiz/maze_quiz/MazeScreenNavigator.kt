package com.infinitepower.newquiz.maze_quiz

import com.infinitepower.newquiz.model.maze.MazeQuiz

interface MazeScreenNavigator {
    fun navigateToGame(item: MazeQuiz.MazeItem)
}