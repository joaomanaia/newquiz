package com.infinitepower.newquiz.domain.repository.math_quiz.maze

import androidx.annotation.IntRange
import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.model.math_quiz.maze.MathQuizMaze
import com.infinitepower.newquiz.model.math_quiz.maze.MazePoint
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.random.Random

interface MazeMathQuizRepository {
    suspend fun generateMaze(
        @IntRange(from = 1, to = 100) questionSize: Int = 50,
        operatorSize: Int = 1,
        difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
        random: Random = Random
    ): MathQuizMaze

    suspend fun saveMaze(maze: MathQuizMaze)

    fun getSavedMazeQuizFlow(): FlowResource<MathQuizMaze>
}