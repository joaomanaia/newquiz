package com.infinitepower.newquiz.core.remote_config

import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizHelperValueState
import com.infinitepower.newquiz.model.question.QuestionDifficulty

@JvmInline
value class RemoteConfigValue<T>(
    val key: String
) {
    companion object {
        val COUNTRIES_AND_CAPITALS = RemoteConfigValue<String>("countries_and_capitals")

        val MAZE_QUIZ_GENERATED_QUESTIONS = RemoteConfigValue<Int>("maze_quiz_generated_questions")

        val FLAG_BASE_URL = RemoteConfigValue<String>("flag_base_url")

        val DEFAULT_SHOW_CATEGORY_CONNECTION_INFO = RemoteConfigValue<ShowCategoryConnectionInfo>("default_show_category_connection_info")

        val DAILY_CHALLENGE_TASKS_TO_GENERATE = RemoteConfigValue<Int>("daily_challenge_tasks_to_generate")

        val DAILY_CHALLENGE_ITEM_REWARD = RemoteConfigValue<Int>("daily_challenge_item_reward")

        val COMPARISON_QUIZ_CATEGORIES = RemoteConfigValue<String>("comparison_quiz_categories")

        val COMPARISON_QUIZ_SKIP_COST = RemoteConfigValue<Int>("comparison_quiz_skip_cost")

        val COMPARISON_QUIZ_FIRST_ITEM_HELPER_VALUE = RemoteConfigValue<ComparisonQuizHelperValueState>("comparison_quiz_first_item_helper_value")

        val USER_INITIAL_DIAMONDS = RemoteConfigValue<Int>("user_initial_diamonds")

        val ALL_LOGOS_QUIZ = RemoteConfigValue<String>("all_logos_quiz")

        val MULTICHOICE_QUICKQUIZ_DIFFICULTY = RemoteConfigValue<String>("multichoice_quickquiz_difficulty")

        val MATH_QUIZ_OPERATOR_SIZE = RemoteConfigValue<Int>("math_quiz_operator_size")

        val MATH_QUIZ_DIFFICULTY = RemoteConfigValue<QuestionDifficulty>("math_quiz_difficulty")
    }
}
