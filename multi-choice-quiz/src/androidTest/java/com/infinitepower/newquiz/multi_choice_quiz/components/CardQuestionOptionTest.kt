package com.infinitepower.newquiz.multi_choice_quiz.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.infinitepower.newquiz.model.multi_choice_quiz.SelectedAnswer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMaterial3Api::class)
class CardQuestionOptionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testButtonsClick() {
        val answers = listOf("A", "B", "C", "D")

        composeTestRule.setContent {
            val (selectedAnswer, setSelectedAnswer) = remember {
                mutableStateOf(SelectedAnswer.NONE)
            }

            Surface {
                CardQuestionAnswers(
                    modifier = Modifier.padding(16.dp),
                    answers = answers,
                    selectedAnswer = selectedAnswer,
                    onOptionClick = setSelectedAnswer
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Answers")
            .onChildren()
            .assertCountEquals(answers.size)
            .assertAll(isNotSelected())
            .assertAll(hasClickAction())
            .onFirst()
            .performClick()
            .assertIsSelected()
    }

    @Test
    fun testButtonClick() {
        composeTestRule.setContent {
            val (selected, setSelected) = remember {
                mutableStateOf(false)
            }

            val invertSelect = { setSelected(!selected) }

            Surface {
                CardQuestionAnswer(
                    description = "Test",
                    selected = selected,
                    onClick = invertSelect,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        composeTestRule
            .onNodeWithText("Test")
            .assertHasClickAction()
            .assertIsNotSelected()
            .performClick()
            .assertIsSelected()
    }
}