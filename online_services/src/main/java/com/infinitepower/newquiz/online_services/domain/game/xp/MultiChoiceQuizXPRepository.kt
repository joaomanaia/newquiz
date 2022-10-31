package com.infinitepower.newquiz.online_services.domain.game.xp

import com.infinitepower.newquiz.data.local.question.QuestionDifficulty
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep

interface MultiChoiceQuizXPRepository : XPRepository {
    fun getXpMultiplierFactor(difficulty: QuestionDifficulty): Float

    fun generateRandomXP(difficulty: QuestionDifficulty): Int

    fun generateQuestionsRandomXP(
        questionSteps: List<MultiChoiceQuestionStep.Completed>
    ): Int
}