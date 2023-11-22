package com.infinitepower.newquiz.core.user_services.data.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.util.kotlin.times
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [MultiChoiceQuizXpGeneratorImpl].
 */
internal class MultiChoiceQuizXpGeneratorImplTest {
    private lateinit var multiChoiceQuizXpGeneratorImpl: MultiChoiceQuizXpGeneratorImpl

    @BeforeTest
    fun setUp() {
        multiChoiceQuizXpGeneratorImpl = MultiChoiceQuizXpGeneratorImpl()
    }

    @Test
    fun `generateRandomXp when all question steps are incorrect, should return 0`() {
        val questionSteps = List(5) {
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = false,
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        val randomXp = multiChoiceQuizXpGeneratorImpl.generateRandomXp(questionSteps)

        assertThat(randomXp).isEqualTo(0u)
    }

    @Test
    fun `test generateRandomXp`() {
        val questionSteps = List(5) {
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = Random.nextBoolean(),
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        val randomXp = multiChoiceQuizXpGeneratorImpl.generateRandomXp(questionSteps)

        val correctCount = questionSteps.count { it.correct }
        val minXpPossible = multiChoiceQuizXpGeneratorImpl.generatedXpRange.first * correctCount.toFloat()

        val hardMultiplierFactor = multiChoiceQuizXpGeneratorImpl.multiplierFactor(QuestionDifficulty.Hard) // Highest multiplier factor
        val maxXpPossible = multiChoiceQuizXpGeneratorImpl.generatedXpRange.last * correctCount.toFloat() * hardMultiplierFactor

        assertThat(randomXp).isIn(minXpPossible..maxXpPossible)
    }
}