package com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz

interface MultiChoiceQuizLoggingAnalytics {
    fun logGameStart(
        questionsSize: Int,
        category: Int? = null,
        difficulty: String? = null
    )

    fun logGameEnd(
        questionsSize: Int,
        correctAnswers: Int
    )
}