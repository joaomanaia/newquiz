package com.infinitepower.newquiz.maze_quiz

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.maze.emptyMaze

@Keep
data class MazeScreenUiState(
    val mathMaze: MazeQuiz = emptyMaze(),
    val loading: Boolean = true
) {
    val isMazeEmpty: Boolean
        get() = mathMaze.items.isEmpty()

    val mazeSeed: Int?
        get() = mathMaze.items.firstOrNull()?.mazeSeed
}