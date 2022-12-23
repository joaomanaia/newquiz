package com.infinitepower.newquiz.maze_quiz

sealed class MazeScreenUiEvent {
    data class GenerateMaze(
        val seed: Int?
    ) : MazeScreenUiEvent()

    object RestartMaze : MazeScreenUiEvent()
}
