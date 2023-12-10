package com.infinitepower.newquiz.core.user_services.data.xp

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import io.mockk.every
import io.mockk.mockk
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [MultiChoiceQuizXpGeneratorImpl].
 */
internal class MultiChoiceQuizXpGeneratorImplTest {
    private lateinit var multiChoiceQuizXpGeneratorImpl: MultiChoiceQuizXpGeneratorImpl

    private val remoteConfig: RemoteConfig = mockk()

    @BeforeTest
    fun setUp() {
        every { remoteConfig.get(RemoteConfigValue.MULTICHOICE_QUIZ_DEFAULT_XP_REWARD) } returns """
            {
                "easy": 10,
                "medium": 20,
                "hard": 30
            }
        """.trimIndent()

        multiChoiceQuizXpGeneratorImpl = MultiChoiceQuizXpGeneratorImpl(
            remoteConfig = remoteConfig
        )
    }

    @Test
    fun `getDefaultXpReward should return the default XP reward`() {
        val defaultXpReward = multiChoiceQuizXpGeneratorImpl.getDefaultXpReward()

        assertThat(defaultXpReward).isEqualTo(
            mapOf(
                QuestionDifficulty.Easy to 10u,
                QuestionDifficulty.Medium to 20u,
                QuestionDifficulty.Hard to 30u
            )
        )
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
        // Add a skipped question step to test that it is not counted
        } + MultiChoiceQuestionStep.Completed(
            question = getBasicMultiChoiceQuestion(),
            correct = true,
            selectedAnswer = SelectedAnswer.NONE,
            skipped = true
        )

        val generatedXp = multiChoiceQuizXpGeneratorImpl.generateXp(questionSteps)

        val defaultXpReward = multiChoiceQuizXpGeneratorImpl.getDefaultXpReward()

        val expectedXp = questionSteps
            .filter { step ->
                step.correct && !step.skipped
            }.sumOf { step ->
                val difficulty = step.question.difficulty

                defaultXpReward[difficulty] ?: 0u
            }

        assertThat(generatedXp).isEqualTo(expectedXp)
    }
}