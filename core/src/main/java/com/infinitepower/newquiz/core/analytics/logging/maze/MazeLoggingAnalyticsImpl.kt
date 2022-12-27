package com.infinitepower.newquiz.core.analytics.logging.maze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.core.analytics.logging.param
import javax.inject.Inject
import javax.inject.Singleton

private const val EVENT_CREATE_MAZE = "create_maze"
private const val EVENT_MAZE_ITEM_CLICK = "maze_item_click"
private const val EVENT_RESTART_MAZE = "restart_maze"
private const val EVENT_MAZE_ITEM_PLAYED = "maze_item_played"
private const val EVENT_MAZE_COMPLETED = "maze_completed"

private const val PARAM_SEED = "seed"
private const val PARAM_ITEM_SIZE = "item_size"
private const val PARAM_GAME_MODES = "game_modes"
private const val PARAM_INDEX = "index"
private const val PARAM_CORRECT = "correct"

@Singleton
class MazeLoggingAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : MazeLoggingAnalytics {
    override fun logCreateMaze(seed: Int, itemSize: Int, gameModes: List<Int>) {
        firebaseAnalytics.logEvent(EVENT_CREATE_MAZE) {
            param(PARAM_SEED, seed)
            param(PARAM_ITEM_SIZE, itemSize)
            param(PARAM_GAME_MODES, gameModes.toString())
        }
    }

    override fun logRestartMaze() {
        firebaseAnalytics.logEvent(EVENT_RESTART_MAZE, null)
    }

    override fun logMazeItemClick(index: Int) {
        firebaseAnalytics.logEvent(EVENT_MAZE_ITEM_CLICK) {
            param(PARAM_INDEX, index)
        }
    }

    override fun logMazeItemPlayed(correct: Boolean) {
        firebaseAnalytics.logEvent(EVENT_MAZE_ITEM_PLAYED) {
            param(PARAM_CORRECT, correct)
        }
    }

    override fun logMazeCompleted(questionSize: Int) {
        firebaseAnalytics.logEvent(EVENT_MAZE_COMPLETED) {
            param(PARAM_ITEM_SIZE, questionSize)
        }
    }
}

@Composable
fun rememberMazeLoggingAnalytics(): MazeLoggingAnalytics {
    return remember {
        val firebaseAnalytics = Firebase.analytics

        MazeLoggingAnalyticsImpl(firebaseAnalytics)
    }
}