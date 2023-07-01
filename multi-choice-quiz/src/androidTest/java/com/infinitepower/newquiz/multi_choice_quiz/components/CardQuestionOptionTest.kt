package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core_test.utils.setTestContent
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class CardQuestionOptionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testButtonClick_whenNotSelected() {
        var clicked by mutableStateOf(false)

        composeTestRule.setTestContent {
            CardQuestionAnswer(
                answer = "Test",
                selected = clicked,
                onClick = { clicked = true }
            )
        }

        composeTestRule
            .onNodeWithText("Test")
            .assertHasClickAction()
            .assertIsNotSelected()
            .performClick()
            .assertIsSelected()

        assertThat(clicked).isTrue()
    }

    @Test
    fun testButtonClick_whenSelected() {
        var clicked by mutableStateOf(true)

        composeTestRule.setTestContent {
            CardQuestionAnswer(
                answer = "Test",
                selected = clicked,
                onClick = { clicked = true }
            )
        }

        composeTestRule
            .onNodeWithText("Test")
            .assertHasClickAction()
            .assertIsSelected()
            .performClick()
            .assertIsSelected()

        assertThat(clicked).isTrue()
    }

    @Test
    fun testButtonsClick() {
        val answers = listOf("A", "B", "C", "D")

        var selectedAnswer: SelectedAnswer by mutableStateOf(SelectedAnswer.NONE)

        composeTestRule.setTestContent {
            CardQuestionAnswers(
                modifier = Modifier.testTag("Answers"),
                answers = answers,
                selectedAnswer = selectedAnswer,
                onOptionClick = { selectedAnswer = it }
            )
        }

        composeTestRule
            .onNodeWithTag("Answers")
            .assertContentDescriptionEquals("Answers container")
            .onChildren()
            .assertCountEquals(answers.size)
            .assertAll(isNotSelected() and hasClickAction())
            .onFirst()
            .performClick()
            .assertIsSelected()

        composeTestRule
            .onNodeWithTag("Answers")
            .onChildren()
            .filterToOne(isSelected())
    }
}