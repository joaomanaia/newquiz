package com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.infinitepower.newquiz.core.analytics.logging.param
import javax.inject.Inject
import javax.inject.Singleton

private const val EVENT_MULTI_CHOICE_GAME_START = "multi_choice_game_start"
private const val EVENT_MULTI_CHOICE_GAME_END = "multi_choice_game_end"

private const val PARAM_QUESTIONS_SIZE = "multi_choice_questions_size"
private const val PARAM_CORRECT_ANSWERS = "multi_choice_correct_answers"
private const val PARAM_CATEGORY = "multi_choice_category"
private const val PARAM_DIFFICULTY = "multi_choice_difficulty"
private const val PARAM_MAZE_ITEM_ID = "maze_item_id"

@Singleton
class MultiChoiceQuizLoggingAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : MultiChoiceQuizLoggingAnalytics {
    override fun logGameStart(
        questionsSize: Int,
        category: Int?,
        difficulty: String?,
        mazeItemId: Int?
    ) {
        firebaseAnalytics.logEvent(EVENT_MULTI_CHOICE_GAME_START) {
            param(PARAM_QUESTIONS_SIZE, questionsSize)
            param(PARAM_CATEGORY, category.toString())
            param(PARAM_DIFFICULTY, difficulty.toString())
            param(PARAM_MAZE_ITEM_ID, mazeItemId.toString())
        }
    }

    override fun logGameEnd(questionsSize: Int, correctAnswers: Int, mazeItemId: Int?) {
        firebaseAnalytics.logEvent(EVENT_MULTI_CHOICE_GAME_END) {
            param(PARAM_QUESTIONS_SIZE, questionsSize)
            param(PARAM_CORRECT_ANSWERS, correctAnswers)
            param(PARAM_MAZE_ITEM_ID, mazeItemId.toString())
        }
    }
}