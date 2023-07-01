package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core_test.utils.setTestContent
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for [QuizStepViewRow]
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
class QuizStepViewRowTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun quizStepViewRow_WithQuestionSteps_whenGameScreen() {
        var clicked = false

        val questionSteps = listOf(
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = true
            ),
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = false
            ),
            MultiChoiceQuestionStep.Current(question = getBasicMultiChoiceQuestion()),
            MultiChoiceQuestionStep.NotCurrent(question = getBasicMultiChoiceQuestion())
        )

        composeTestRule.setTestContent {
            QuizStepViewRow(
                questionSteps = questionSteps,
                isResultsScreen = false, // Game screen
                onClick = { _, _ -> clicked = true },
                modifier = Modifier.testTag("QuizStepViewRow")
            )
        }

        composeTestRule
            .onNodeWithTag("QuizStepViewRow")
            .onChildren()
            .filter(isNotEnabled())
            .assertCountEquals(4)
            .apply {
                onFirst().assertContentDescriptionEquals("Question 1 - Correct")
                this[1].assertContentDescriptionEquals("Question 2 - Incorrect")
                this[2].assertTextEquals("3")
                onLast()
                    .assertTextEquals("4")
                    .performClick()
            }

        assertThat(clicked).isFalse()
    }

    @Test
    fun quizStepViewRow_WithQuestionSteps_whenResultsScreen() {
        var clickedIndex = -1
        var clickedQuestionStep: MultiChoiceQuestionStep? = null

        val questionSteps = listOf(
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = true
            ),
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = false
            ),
            MultiChoiceQuestionStep.Current(question = getBasicMultiChoiceQuestion()),
            MultiChoiceQuestionStep.NotCurrent(question = getBasicMultiChoiceQuestion())
        )

        composeTestRule.setTestContent {
            QuizStepViewRow(
                questionSteps = questionSteps,
                isResultsScreen = true, // Results screen
                onClick = { index, questionStep ->
                    clickedIndex = index
                    clickedQuestionStep = questionStep
                },
                modifier = Modifier.testTag("QuizStepViewRow")
            )
        }

        composeTestRule
            .onNodeWithTag("QuizStepViewRow")
            .onChildren()
            .filter(isEnabled() and hasClickAction())
            .assertCountEquals(4)
            .apply {
                onFirst().assertContentDescriptionEquals("Question 1 - Correct")
                this[1].assertContentDescriptionEquals("Question 2 - Incorrect")
                this[2].assertTextEquals("3")
                onLast()
                    .assertTextEquals("4")
                    .performClick()
            }

        assertThat(clickedIndex).isEqualTo(3)
        assertThat(clickedQuestionStep).isEqualTo(questionSteps[3])
    }
}