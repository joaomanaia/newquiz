package com.infinitepower.newquiz.core.user_services.domain.xp

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.question.QuestionDifficulty

interface MultiChoiceQuizXpGenerator : XpGenerator {
    fun getDefaultXpForDifficulty(difficulty: QuestionDifficulty): UInt {
        return when (difficulty) {
            QuestionDifficulty.Easy -> 10u
            QuestionDifficulty.Medium -> 15u
            QuestionDifficulty.Hard -> 20u
        }
    }

    /**
     * Generates a amount of XP based on the given question steps.
     */
    fun generateXp(
        questionSteps: List<MultiChoiceQuestionStep.Completed>
    ): UInt
}