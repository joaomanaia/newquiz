package com.infinitepower.newquiz.core.analytics.logging.wordle

import android.util.Log

class LocalWordleLoggingAnalyticsImpl : WordleLoggingAnalytics {
    companion object {
        const val TAG = "WordleAnalytics"
    }

    override fun logGameStart(
        wordLength: Int,
        maxRows: Int,
        quizType: String,
        day: String?,
        mazeItemId: Int?
    ) {
        Log.d(
            TAG,
            "Game Start: Word length: $wordLength, Quiz max rows: $maxRows, Quiz type: $quizType, Word day: $day, Maze item id: $mazeItemId"
        )
    }

    override fun logGameEnd(
        wordLength: Int,
        maxRows: Int,
        lastRow: Int,
        lastRowCorrect: Boolean,
        quizType: String,
        day: String?,
        mazeItemId: Int?
    ) {
        Log.d(
            TAG,
            "Game end: Word length: $wordLength, Quiz max rows: $maxRows, Last row position: $lastRow, Is last round correct: $lastRowCorrect, Quiz type: $quizType, Word day: $day, Maze item id: $mazeItemId"
        )
    }
}