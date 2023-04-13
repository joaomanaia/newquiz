package com.infinitepower.newquiz.core.analytics.logging

interface CoreLoggingAnalytics {
    fun enableLoggingAnalytics(enabled: Boolean)

    fun logNewLevel(
        level: Int,
        diamondsEarned: Int
    )

    fun logSpendDiamonds(amount: Int, usedFor: String)

    fun setWordleLangUserProperty(lang: String)

    fun setTranslatorModelDownloaded(downloaded: Boolean)
}