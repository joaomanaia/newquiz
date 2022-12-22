package com.infinitepower.newquiz.math_quiz.maze

sealed class MathMazeScreenUiEvent {
    data class GenerateMaze(
        val seed: Int?
    ) : MathMazeScreenUiEvent()

    object RestartMaze : MathMazeScreenUiEvent()
}
