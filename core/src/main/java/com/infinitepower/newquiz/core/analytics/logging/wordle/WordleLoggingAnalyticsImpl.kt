package com.infinitepower.newquiz.core.analytics.logging.wordle

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.infinitepower.newquiz.core.analytics.logging.param
import javax.inject.Inject
import javax.inject.Singleton

private const val EVENT_WORDLE_GAME_START = "wordle_game_start"
private const val EVENT_WORDLE_GAME_END = "wordle_row_end"
private const val EVENT_DAILY_WORDLE_ITEM_CLICK = "daily_wordle_item_click"
private const val EVENT_DAILY_WORDLE_ITEM_COMPLETE = "daily_wordle_item_complete"

private const val PARAM_WORD_LENGTH = "wordle_word_length"
private const val PARAM_MAX_ROWS = "wordle_max_rows"
private const val PARAM_DAY = "wordle_day"
private const val PARAM_LAST_ROW = "wordle_last_row"
private const val PARAM_WORDLE_QUIZ_TYPE = "wordle_quiz_type"
private const val PARAM_LAST_ROW_CORRECT = "wordle_last_row_correct"
private const val PARAM_MAZE_ITEM_ID = "maze_item_id"

@Singleton
class WordleLoggingAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : WordleLoggingAnalytics {
    override fun logGameStart(
        wordLength: Int,
        maxRows: Int,
        quizType: String,
        day: String?,
        mazeItemId: Int?
    ) {
        firebaseAnalytics.logEvent(EVENT_WORDLE_GAME_START) {
            param(PARAM_WORD_LENGTH, wordLength)
            param(PARAM_MAX_ROWS, maxRows)
            param(PARAM_WORDLE_QUIZ_TYPE, quizType)
            param(PARAM_DAY, day.toString())
            param(PARAM_MAZE_ITEM_ID, mazeItemId.toString())
        }
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
        firebaseAnalytics.logEvent(EVENT_WORDLE_GAME_END) {
            param(PARAM_WORD_LENGTH, wordLength)
            param(PARAM_MAX_ROWS, maxRows)
            param(PARAM_LAST_ROW, lastRow)
            param(PARAM_LAST_ROW_CORRECT, lastRowCorrect)
            param(PARAM_WORDLE_QUIZ_TYPE, quizType)
            param(PARAM_DAY, day.toString())
            param(PARAM_MAZE_ITEM_ID, mazeItemId.toString())
        }
    }

    override fun logDailyWordleItemClick(wordLength: Int, day: String) {
        firebaseAnalytics.logEvent(EVENT_DAILY_WORDLE_ITEM_CLICK) {
            param(PARAM_WORD_LENGTH, wordLength)
            param(PARAM_DAY, day)
        }
    }

    override fun logDailyWordleItemComplete(wordLength: Int, day: String, correct: Boolean) {
        firebaseAnalytics.logEvent(EVENT_DAILY_WORDLE_ITEM_COMPLETE) {
            param(PARAM_WORD_LENGTH, wordLength)
            param(PARAM_LAST_ROW_CORRECT, correct)
        }
    }
}