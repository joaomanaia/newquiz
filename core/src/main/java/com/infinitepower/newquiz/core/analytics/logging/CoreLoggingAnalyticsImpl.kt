package com.infinitepower.newquiz.core.analytics.logging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

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

    override fun logNewLevel(level: Int, diamondsEarned: Int) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.EARN_VIRTUAL_CURRENCY) {
            param(FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME, "diamonds")
            param(FirebaseAnalytics.Param.VALUE, diamondsEarned)
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP) {
            param(FirebaseAnalytics.Param.LEVEL, level)
            param(FirebaseAnalytics.Param.CHARACTER, "global")
        }
    }

    override fun logSpendDiamonds(amount: Int, usedFor: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY) {
            param(FirebaseAnalytics.Param.VALUE, amount)
            param(FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME, "diamonds")
            param(FirebaseAnalytics.Param.ITEM_NAME, usedFor)
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