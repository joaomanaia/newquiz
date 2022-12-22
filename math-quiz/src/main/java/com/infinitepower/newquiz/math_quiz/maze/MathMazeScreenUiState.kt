package com.infinitepower.newquiz.math_quiz.maze

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.math_quiz.maze.MathQuizMaze
import com.infinitepower.newquiz.model.math_quiz.maze.emptyMathMaze

@Keep
data class MathMazeScreenUiState(
    val mathMaze: MathQuizMaze = emptyMathMaze(),
    val loading: Boolean = true
) {
    val isMazeEmpty: Boolean
        get() = mathMaze.formulas.isEmpty()
}