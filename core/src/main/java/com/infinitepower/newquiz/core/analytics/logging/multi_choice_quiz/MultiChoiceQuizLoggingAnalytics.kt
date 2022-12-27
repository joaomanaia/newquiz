package com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz

interface MultiChoiceQuizLoggingAnalytics {
    fun logGameStart(
        questionsSize: Int,
        category: Int? = null,
        difficulty: String? = null,
        mazeItemId: Int? = null
    )

    fun logGameEnd(
        questionsSize: Int,
        correctAnswers: Int,
        mazeItemId: Int? = null
    )

    fun logCategoryClicked(id: Int)

    fun logSaveQuestion()

    fun logDownloadQuestions()

    fun logPlaySavedQuestions(questionsSize: Int)
}