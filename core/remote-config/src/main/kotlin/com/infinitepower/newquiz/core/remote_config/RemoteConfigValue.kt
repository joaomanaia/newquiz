package com.infinitepower.newquiz.core.remote_config

import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizHelperValueState
import com.infinitepower.newquiz.model.question.QuestionDifficulty

@JvmInline
value class RemoteConfigValue<T>(
    val key: String
) {
    companion object {
        val MAZE_QUIZ_GENERATED_QUESTIONS = RemoteConfigValue<Int>("maze_quiz_generated_questions")

        val FLAG_BASE_URL = RemoteConfigValue<String>("flag_base_url")

        val DEFAULT_SHOW_CATEGORY_CONNECTION_INFO = RemoteConfigValue<ShowCategoryConnectionInfo>(
            key = "default_show_category_connection_info"
        )

        val DAILY_CHALLENGE_TASKS_TO_GENERATE = RemoteConfigValue<Int>("daily_challenge_tasks_to_generate")

        val DAILY_CHALLENGE_ITEM_REWARD = RemoteConfigValue<Int>("daily_challenge_item_reward")

        val COMPARISON_QUIZ_CATEGORIES = RemoteConfigValue<String>("comparison_quiz_categories")

        val COMPARISON_QUIZ_SKIP_COST = RemoteConfigValue<Int>("comparison_quiz_skip_cost")

        val COMPARISON_QUIZ_FIRST_ITEM_HELPER_VALUE = RemoteConfigValue<ComparisonQuizHelperValueState>(
            key = "comparison_quiz_first_item_helper_value"
        )

        val USER_INITIAL_DIAMONDS = RemoteConfigValue<Int>("user_initial_diamonds")

        val COMPARISON_QUIZ_DEFAULT_XP_REWARD = RemoteConfigValue<Int>("comparison_quiz_default_xp_reward")

        val WORDLE_DEFAULT_XP_REWARD = RemoteConfigValue<Int>("wordle_default_xp_reward")

        val MULTICHOICE_QUIZ_DEFAULT_XP_REWARD = RemoteConfigValue<String>("multichoice_quiz_default_xp_reward")

        val ALL_LOGOS_QUIZ = RemoteConfigValue<String>("all_logos_quiz")

        val MULTICHOICE_QUICKQUIZ_DIFFICULTY = RemoteConfigValue<String>("multichoice_quickquiz_difficulty")

        val MATH_QUIZ_OPERATOR_SIZE = RemoteConfigValue<Int>("math_quiz_operator_size")

        val MATH_QUIZ_DIFFICULTY = RemoteConfigValue<QuestionDifficulty>("math_quiz_difficulty")

        val NEW_LEVEL_DIAMONDS = RemoteConfigValue<Int>("new_level_diamonds")

        val MULTICHOICE_SKIP_COST = RemoteConfigValue<Int>("multichoice_skip_cost")
    }
}
