package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.testing.utils.setTestContent
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for [QuizStepView]
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
internal class QuizStepViewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun quizStepView_whenCompletedCorrectAnswer_showsCheckIcon() {
        var clicked = false

        composeTestRule.setTestContent {
            QuizStepView(
                questionStep = MultiChoiceQuestionStep.Completed(
                    question = getBasicMultiChoiceQuestion(),
                    correct = true
                ),
                position = 1,
                enabled = false,
                onClick = { clicked = true },
                modifier = Modifier.testTag("QuizStepView")
            )
        }

        composeTestRule.onNodeWithText("1").assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("QuizStepView")
            .assertIsNotEnabled()
            .assertWidthIsEqualTo(35.dp)
            .assertHeightIsEqualTo(35.dp)
            .assertContentDescriptionEquals("Question 1 - Correct")

        assertThat(clicked).isFalse()
    }

    @Test
    fun quizStepView_whenCompletedIncorrectAnswer_showsCloseIcon() {
        composeTestRule.setTestContent {
            QuizStepView(
                questionStep = MultiChoiceQuestionStep.Completed(
                    question = getBasicMultiChoiceQuestion(),
                    correct = false
                ),
                position = 1,
                enabled = false,
                onClick = {},
                modifier = Modifier.testTag("QuizStepView")
            )
        }

        composeTestRule.onNodeWithText("1").assertDoesNotExist()

        composeTestRule
            .onNodeWithTag("QuizStepView")
            .assertIsNotEnabled()
            .assertWidthIsEqualTo(35.dp)
            .assertHeightIsEqualTo(35.dp)
            .assertContentDescriptionEquals("Question 1 - Incorrect")
    }

    @Test
    fun quizStepView_whenCurrentStep_showsPosition() {
        composeTestRule.setTestContent {
            QuizStepView(
                questionStep = MultiChoiceQuestionStep.Current(
                    question = getBasicMultiChoiceQuestion()
                ),
                position = 1,
                enabled = false,
                onClick = {},
                modifier = Modifier.testTag("QuizStepView")
            )
        }

        composeTestRule
            .onNodeWithTag("QuizStepView")
            .assertTextEquals("1")
            .assertIsNotEnabled()
            .assertWidthIsEqualTo(35.dp)
            .assertHeightIsEqualTo(35.dp)
    }

    @Test
    fun quizStepView_whenNotCurrentStep_showsPosition() {
        composeTestRule.setTestContent {
            QuizStepView(
                questionStep = MultiChoiceQuestionStep.NotCurrent(
                    question = getBasicMultiChoiceQuestion()
                ),
                position = 1,
                enabled = false,
                onClick = {},
                modifier = Modifier.testTag("QuizStepView")
            )
        }

        composeTestRule
            .onNodeWithTag("QuizStepView")
            .assertTextEquals("1")
            .assertIsNotEnabled()
            .assertWidthIsEqualTo(35.dp)
            .assertHeightIsEqualTo(35.dp)
    }

    @Test
    fun quizStepView_whenEnabled_shouldClick() {
        var clicked = false

        composeTestRule.setTestContent {
            QuizStepView(
                questionStep = MultiChoiceQuestionStep.Completed(
                    question = getBasicMultiChoiceQuestion(),
                    correct = true
                ),
                position = 1,
                enabled = true,
                onClick = { clicked = true },
                modifier = Modifier.testTag("QuizStepView")
            )
        }

        composeTestRule
            .onNodeWithTag("QuizStepView")
            .assertIsEnabled()
            .assertHasClickAction()
            .assertWidthIsEqualTo(35.dp)
            .assertHeightIsEqualTo(35.dp)
            .assertContentDescriptionEquals("Question 1 - Correct")
            .performClick()

        assertThat(clicked).isTrue()
    }
}