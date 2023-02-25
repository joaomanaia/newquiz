package com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory

interface MultiChoiceQuizLoggingAnalytics {
    fun logGameStart(
        questionsSize: Int,
        category: MultiChoiceBaseCategory,
        difficulty: String? = null,
        mazeItemId: Int? = null
    )

    fun logGameEnd(
        questionsSize: Int,
        correctAnswers: Int,
        mazeItemId: Int? = null
    )

    fun logCategoryClicked(category: MultiChoiceBaseCategory)

    fun logSaveQuestion()

    fun logDownloadQuestions()

    fun logPlaySavedQuestions(questionsSize: Int)
}