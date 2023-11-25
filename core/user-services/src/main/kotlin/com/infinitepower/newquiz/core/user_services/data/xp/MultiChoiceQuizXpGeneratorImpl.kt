package com.infinitepower.newquiz.core.user_services.data.xp

import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultiChoiceQuizXpGeneratorImpl @Inject constructor() : MultiChoiceQuizXpGenerator {
    override fun generateXp(
        questionSteps: List<MultiChoiceQuestionStep.Completed>
    ): UInt {
        return questionSteps
            .filter(MultiChoiceQuestionStep.Completed::correct)
            .sumOf { step ->
                val difficulty = step.question.difficulty

                getDefaultXpForDifficulty(difficulty)
            }
    }
}