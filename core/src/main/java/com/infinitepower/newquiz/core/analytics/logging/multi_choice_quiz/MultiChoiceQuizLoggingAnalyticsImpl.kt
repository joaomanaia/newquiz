package com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.infinitepower.newquiz.core.analytics.logging.param
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import javax.inject.Inject
import javax.inject.Singleton

private const val EVENT_MULTI_CHOICE_GAME_START = "multi_choice_game_start"
private const val EVENT_MULTI_CHOICE_GAME_END = "multi_choice_game_end"
private const val EVENT_CATEGORY_CLICKED = "multi_choice_category_clicked"
private const val EVENT_SAVE_QUESTION = "multi_choice_save_question"
private const val EVENT_DOWNLOAD_QUESTIONS = "multi_choice_save_question"
private const val EVENT_PLAY_SAVED_QUESTIONS = "multi_choice_play_saved_questions"

private const val PARAM_QUESTIONS_SIZE = "multi_choice_questions_size"
private const val PARAM_CORRECT_ANSWERS = "multi_choice_correct_answers"
private const val PARAM_CATEGORY = "multi_choice_category"
private const val PARAM_DIFFICULTY = "multi_choice_difficulty"
private const val PARAM_MAZE_ITEM_ID = "maze_item_id"
private const val PARAM_ID = "id"

@Singleton
class MultiChoiceQuizLoggingAnalyticsImpl @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : MultiChoiceQuizLoggingAnalytics {
    override fun logGameStart(
        questionsSize: Int,
        category: MultiChoiceBaseCategory,
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

    override fun logCategoryClicked(category: MultiChoiceBaseCategory) {
        firebaseAnalytics.logEvent(EVENT_CATEGORY_CLICKED) {
            param(PARAM_ID, category.key)
        }
    }

    override fun logSaveQuestion() {
        firebaseAnalytics.logEvent(EVENT_SAVE_QUESTION, null)
    }

    override fun logDownloadQuestions() {
        firebaseAnalytics.logEvent(EVENT_DOWNLOAD_QUESTIONS, null)
    }

    override fun logPlaySavedQuestions(questionsSize: Int) {
        firebaseAnalytics.logEvent(EVENT_PLAY_SAVED_QUESTIONS) {
            param(PARAM_QUESTIONS_SIZE, questionsSize)
        }
    }
}

@Composable
fun rememberMultiChoiceLoggingAnalytics(): MultiChoiceQuizLoggingAnalytics {
    return remember {
        val firebaseAnalytics = Firebase.analytics

        MultiChoiceQuizLoggingAnalyticsImpl(firebaseAnalytics)
    }
}