package com.infinitepower.newquiz.core.analytics.logging

interface CoreLoggingAnalytics {
    fun logScreenView(
        screenName: String,
        screenClass: String? = null
    )

    fun logNewLevel(
        level: Int,
        diamondsEarned: Int
    )

    fun logSpendDiamonds(amount: Int, usedFor: String)
}