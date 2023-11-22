package com.infinitepower.newquiz.core.user_services.data.xp

import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MultiChoiceQuizXpGeneratorImpl @Inject constructor() : MultiChoiceQuizXpGenerator {
    override fun generateRandomXp(
        questionSteps: List<MultiChoiceQuestionStep.Completed>,
        random: Random
    ): UInt {
        return questionSteps
            .filter(MultiChoiceQuestionStep.Completed::correct)
            .sumOf { step ->
                val difficulty = step.question.difficulty
                generateRandomXp(difficulty = difficulty, random = random)
            }
    }
}