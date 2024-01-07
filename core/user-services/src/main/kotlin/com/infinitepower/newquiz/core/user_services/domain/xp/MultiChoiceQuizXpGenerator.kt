package com.infinitepower.newquiz.core.user_services.domain.xp

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.question.QuestionDifficulty

interface MultiChoiceQuizXpGenerator : XpGenerator {
    fun getDefaultXpReward(): Map<QuestionDifficulty, UInt>

    /**
     * Generates a amount of XP based on the given question steps.
     */
    fun generateXp(
        questionSteps: List<MultiChoiceQuestionStep.Completed>
    ): UInt
}
