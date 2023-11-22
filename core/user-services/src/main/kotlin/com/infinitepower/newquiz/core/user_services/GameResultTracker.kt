package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep

interface GameResultTracker {
    suspend fun saveMultiChoiceGame(
        questionSteps: List<MultiChoiceQuestionStep.Completed>,
        generateXp: Boolean
    )
}