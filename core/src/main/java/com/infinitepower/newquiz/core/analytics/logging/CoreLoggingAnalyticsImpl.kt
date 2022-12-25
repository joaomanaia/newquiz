package com.infinitepower.newquiz.core.analytics.logging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

const val EVENT_CREATE_MAZE = "create_maze"

const val PARAM_SEED = "seed"
const val PARAM_ITEM_SIZE = "item_size"
const val PARAM_GAME_MODES = "game_modes"

@Singleton
class CoreLoggingAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : CoreLoggingAnalytics {
    override fun logScreenView(screenName: String, screenClass: String?) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)

            if (screenClass != null) param(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        }
    }

    override fun logCreateMaze(seed: Int, itemSize: Int, gameModes: List<Int>) {
        firebaseAnalytics.logEvent(EVENT_CREATE_MAZE) {
            param(PARAM_SEED, seed)
            param(PARAM_ITEM_SIZE, itemSize)
            param(PARAM_GAME_MODES, gameModes.toString())
        }
    }
}

@Composable
fun rememberCoreLoggingAnalytics(): CoreLoggingAnalytics {
    return remember {
        val firebaseAnalytics = Firebase.analytics

        CoreLoggingAnalyticsImpl(firebaseAnalytics)
    }
}