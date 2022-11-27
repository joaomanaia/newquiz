package com.infinitepower.newquiz.core.analytics.logging.wordle

interface WordleLoggingAnalytics {
    fun logGameStart(
        wordLength: Int,
        maxRows: Int,
        quizType: String,
        day: String? = null
    )

    fun logGameEnd(
        wordLength: Int,
        maxRows: Int,
        lastRow: Int,
        lastRowCorrect: Boolean,
        quizType: String,
        day: String? = null
    )
}