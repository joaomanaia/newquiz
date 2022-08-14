package com.infinitepower.newquiz.core.analytics.logging.wordle

interface WordleLoggingAnalytics {
    fun logGameStart(
        wordLength: Int,
        maxRows: Int,
        day: String? = null
    )

    fun logGameEnd(
        wordLength: Int,
        maxRows: Int,
        lastRow: Int,
        lastRowCorrect: Boolean,
        day: String? = null
    )
}