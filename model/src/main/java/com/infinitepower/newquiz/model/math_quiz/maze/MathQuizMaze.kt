package com.infinitepower.newquiz.model.math_quiz.maze

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.infinitepower.newquiz.model.math_quiz.MathFormula
import com.infinitepower.newquiz.model.question.QuestionDifficulty

@Keep
data class MathQuizMaze(
    val formulas: List<MazeItem>
) {
    @Keep
    @Entity(tableName = "mazeItems")
    data class MazeItem(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        val formula: MathFormula,
        val difficulty: QuestionDifficulty,
        val played: Boolean = false,
        val correct: Boolean = false
    )
}

fun emptyMathMaze(): MathQuizMaze = MathQuizMaze(formulas = emptyList())