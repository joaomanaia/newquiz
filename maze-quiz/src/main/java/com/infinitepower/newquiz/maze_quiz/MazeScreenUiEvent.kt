package com.infinitepower.newquiz.maze_quiz

sealed class MazeScreenUiEvent {
    data class GenerateMaze(
        val seed: Int?,
        val gamesModeSelected: List<Int>?
    ) : MazeScreenUiEvent()

    object RestartMaze : MazeScreenUiEvent()
}
