package com.infinitepower.newquiz.core.analytics.logging.wordle

interface WordleLoggingAnalytics {
    fun logGameStart(
        wordLength: Int,
        maxRows: Int,
        quizType: String,
        mazeItemId: Int? = null
    )

    fun logGameEnd(
        wordLength: Int,
        maxRows: Int,
        lastRow: Int,
        lastRowCorrect: Boolean,
        quizType: String,
        mazeItemId: Int? = null
    )

    fun logDailyWordleItemClick(wordLength: Int, day: String)
}