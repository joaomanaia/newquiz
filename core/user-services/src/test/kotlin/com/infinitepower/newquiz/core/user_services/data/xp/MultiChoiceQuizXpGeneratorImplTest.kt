package com.infinitepower.newquiz.core.user_services.data.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
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
    fun `generateXp when all question steps are incorrect, should return 0`() {
        val questionSteps = List(5) {
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = false,
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        val randomXp = multiChoiceQuizXpGeneratorImpl.generateXp(questionSteps)

        assertThat(randomXp).isEqualTo(0u)
    }

    @Test
    fun `test generateXp`() {
        val questionSteps = List(5) {
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = Random.nextBoolean(),
                selectedAnswer = SelectedAnswer.NONE
            )
        }

        val generatedXp = multiChoiceQuizXpGeneratorImpl.generateXp(questionSteps)

        val expectedXp = questionSteps
            .filter(MultiChoiceQuestionStep.Completed::correct)
            .sumOf { step ->
                val difficulty = step.question.difficulty

                multiChoiceQuizXpGeneratorImpl.getDefaultXpForDifficulty(difficulty)
            }

        assertThat(generatedXp).isEqualTo(expectedXp)
    }
}