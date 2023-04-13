package com.infinitepower.newquiz.core.analytics.logging

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

private const val WORDLE_LANG_USER_PROPERTY = "wordle_lang"
private const val TRANSLATOR_DOWNLOADED_USER_PROPERTY = "translator_downloaded"

@Singleton
class CoreLoggingAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : CoreLoggingAnalytics {
    override fun enableLoggingAnalytics(enabled: Boolean) {
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled)
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

    override fun setWordleLangUserProperty(lang: String) {
        firebaseAnalytics.setUserProperty(WORDLE_LANG_USER_PROPERTY, lang)
    }

    override fun setTranslatorModelDownloaded(downloaded: Boolean) {
        firebaseAnalytics.setUserProperty(TRANSLATOR_DOWNLOADED_USER_PROPERTY, downloaded)
    }
}

object LocalCoreLoggingAnalytics : CoreLoggingAnalytics {
    override fun enableLoggingAnalytics(enabled: Boolean) {
        Log.d("CoreLogging", "Eisabled: $enabled - logging analytics")
    }

    override fun logNewLevel(level: Int, diamondsEarned: Int) {
        Log.d("CoreLogging", "New level: $level, earned $diamondsEarned diamonds")
    }

    override fun logSpendDiamonds(amount: Int, usedFor: String) {
        Log.d("CoreLogging", "$amount diamonds spent for $usedFor")
    }

    override fun setWordleLangUserProperty(lang: String) {
        Log.d("CoreLogging", "User language property: $lang")
    }

    override fun setTranslatorModelDownloaded(downloaded: Boolean) {
        Log.d("CoreLogging", "User translator downloaded: $downloaded")
    }
}

@Composable
fun rememberCoreLoggingAnalytics(): CoreLoggingAnalytics {
    if (LocalInspectionMode.current) return LocalCoreLoggingAnalytics

    return remember {
        val firebaseAnalytics = Firebase.analytics

        CoreLoggingAnalyticsImpl(firebaseAnalytics)
    }
}