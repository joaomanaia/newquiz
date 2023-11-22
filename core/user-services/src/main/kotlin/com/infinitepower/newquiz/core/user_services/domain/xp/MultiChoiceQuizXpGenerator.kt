package com.infinitepower.newquiz.core.user_services.domain.xp

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import kotlin.random.Random

interface MultiChoiceQuizXpGenerator : XpGenerator {
    override val generatedXpRange: UIntRange
        get() = 10u..20u

    /**
     * Generates a random xp between [generatedXpRange] based if the question was answered correctly.
     */
    fun generateRandomXp(
        questionSteps: List<MultiChoiceQuestionStep.Completed>,
        random: Random = Random
    ): UInt
}