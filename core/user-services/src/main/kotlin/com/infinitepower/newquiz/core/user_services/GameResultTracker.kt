package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep

interface GameResultTracker {
    suspend fun saveMultiChoiceGame(
        questionSteps: List<MultiChoiceQuestionStep.Completed>,
        generateXp: Boolean
    )

    suspend fun saveWordleGame(
        wordLength: UInt,
        rowsUsed: UInt,
        maxRows: Int,
        categoryId: String,
        generateXp: Boolean
    )

    suspend fun saveComparisonQuizGame(
        categoryId: String,
        comparisonMode: String,
        endPosition: UInt,
        highestPosition: UInt,
        skippedAnswers: UInt,
        generateXp: Boolean
    )
}