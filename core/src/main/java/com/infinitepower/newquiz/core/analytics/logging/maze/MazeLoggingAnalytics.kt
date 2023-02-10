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

class LocalMazeLoggingAnalytics : MazeLoggingAnalytics {
    override fun logCreateMaze(seed: Int, itemSize: Int, gameModes: List<Int>) {
        println("Seed: $seed, item size: $itemSize, game modes: $gameModes")
    }

    override fun logRestartMaze() {
        println("Maze restarted")
    }

    override fun logMazeItemClick(index: Int) {
        println("Maze clicked in index: $index")
    }

    override fun logMazeItemPlayed(correct: Boolean) {
        println("Maze item played and got correct -> $correct")
    }

    override fun logMazeCompleted(questionSize: Int) {
        println("Maze completed $questionSize")
    }
}