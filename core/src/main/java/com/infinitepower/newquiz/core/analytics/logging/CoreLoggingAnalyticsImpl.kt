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
}

@Composable
fun rememberCoreLoggingAnalytics(): CoreLoggingAnalytics {
    return remember {
        val firebaseAnalytics = Firebase.analytics

        CoreLoggingAnalyticsImpl(firebaseAnalytics)
    }
}