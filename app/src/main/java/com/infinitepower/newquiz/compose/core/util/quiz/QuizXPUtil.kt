package com.infinitepower.newquiz.compose.core.util.quiz

import com.infinitepower.newquiz.compose.core.quiz.xp.QuizXP
import com.infinitepower.newquiz.compose.model.question.QuestionDifficulty
import com.infinitepower.newquiz.compose.model.question.QuestionStep
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizXPUtil @Inject constructor() : QuizXP() {
    fun getNewUserXPByQuizSteps(
        quizSteps: List<QuestionStep.Completed>
    ): Long = quizSteps.sumOf { step ->
        if (step.correct) {
            val difficulty = QuestionDifficulty.fromKeyName(step.question.difficulty)
            getXp(difficulty)
        } else 0L
    }

    /**
     * Verifies if current user level is equal to new user total xp
     *
     * @param currentLevel current user level
     * @param newTotalXP new total xp
     * @return true if current user level is equal to new user total xp
     */
    fun verifyNewLevel(
        currentLevel: Long,
        newTotalXP: Long
    ) = currentLevel < getLevelByXP(newTotalXP)
}