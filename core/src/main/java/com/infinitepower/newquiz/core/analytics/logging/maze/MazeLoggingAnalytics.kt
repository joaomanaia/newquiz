package com.infinitepower.newquiz.core.analytics.logging.maze

interface MazeLoggingAnalytics {
    fun logCreateMaze(
        seed: Int,
        itemSize: Int,
        gameModes: List<Int>
    )

    fun logRestartMaze()

    fun logMazeItemClick(index: Int)

    fun logMazeItemPlayed(correct: Boolean)

    fun logMazeCompleted(questionSize: Int)
}