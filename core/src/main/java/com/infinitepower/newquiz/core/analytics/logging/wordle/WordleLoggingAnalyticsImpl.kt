package com.infinitepower.newquiz.core.analytics.logging.wordle

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.infinitepower.newquiz.core.analytics.logging.param
import javax.inject.Inject
import javax.inject.Singleton

private const val EVENT_WORDLE_GAME_START = "wordle_game_start"
private const val EVENT_WORDLE_GAME_END = "wordle_row_end"

private const val PARAM_WORD_LENGTH = "wordle_word_length"
private const val PARAM_MAX_ROWS = "wordle_max_rows"
private const val PARAM_DAY = "wordle_day"
private const val PARAM_LAST_ROW = "wordle_last_row"
private const val PARAM_LAST_ROW_CORRECT = "wordle_last_row_correct"

@Singleton
class WordleLoggingAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : WordleLoggingAnalytics {
    override fun logGameStart(wordLength: Int, maxRows: Int, day: String?) {
        firebaseAnalytics.logEvent(EVENT_WORDLE_GAME_START) {
            param(PARAM_WORD_LENGTH, wordLength)
            param(PARAM_MAX_ROWS, maxRows)
            if (day != null) param(PARAM_DAY, day)
        }
    }

    override fun logGameEnd(
        wordLength: Int,
        maxRows: Int,
        lastRow: Int,
        lastRowCorrect: Boolean,
        day: String?
    ) {
        firebaseAnalytics.logEvent(EVENT_WORDLE_GAME_END) {
            param(PARAM_WORD_LENGTH, wordLength)
            param(PARAM_MAX_ROWS, maxRows)
            param(PARAM_LAST_ROW, lastRow)
            param(PARAM_LAST_ROW_CORRECT, lastRowCorrect)
            if (day != null) param(PARAM_DAY, day)
        }
    }
}