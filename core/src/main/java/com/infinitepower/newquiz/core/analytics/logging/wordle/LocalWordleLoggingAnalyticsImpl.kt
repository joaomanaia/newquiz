package com.infinitepower.newquiz.core.analytics.logging.wordle

import android.util.Log

class LocalWordleLoggingAnalyticsImpl : WordleLoggingAnalytics {
    companion object {
        const val TAG = "WordleAnalytics"
    }

    override fun logGameStart(wordLength: Int, maxRows: Int, day: String?) {
        Log.d(TAG, "Game Start: Word length: $wordLength, Quiz max rows: $maxRows, Word day: $day")
    }

    override fun logGameEnd(
        wordLength: Int,
        maxRows: Int,
        lastRow: Int,
        lastRowCorrect: Boolean,
        day: String?
    ) {
        Log.d(
            TAG,
            "Game end: Word length: $wordLength, Quiz max rows: $maxRows, Last row position: $lastRow, Is last round correct: $lastRowCorrect, Word day: $day"
        )
    }

    override fun logRowCompleted(
        wordLength: Int,
        maxRows: Int,
        correctItems: Int,
        presentItems: Int
    ) {
        Log.d(TAG, "Row completed: Word length: $wordLength, Quiz max rows: $maxRows, Correct items: $correctItems, Present items: $presentItems")
    }
}