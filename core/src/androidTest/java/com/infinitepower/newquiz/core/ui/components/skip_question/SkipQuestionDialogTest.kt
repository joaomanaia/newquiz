package com.infinitepower.newquiz.core.ui.components.skip_question

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core_test.compose.theme.NewQuizTestTheme
import com.infinitepower.newquiz.core_test.utils.setTestDeviceLocale
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for [SkipQuestionDialog].
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMaterial3Api::class)
internal class SkipQuestionDialogTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun skipQuestionDialog_CanSkip_ShowAlertDialog() {
        val userDiamonds = 10
        val skipCost = 5
        var skipClickCalled = false
        var dismissClickCalled = false

        composeTestRule.setContent {
            setTestDeviceLocale()

            NewQuizTestTheme {
                SkipQuestionDialog(
                    userDiamonds = userDiamonds,
                    skipCost = skipCost,
                    onSkipClick = { skipClickCalled = true },
                    onDismissClick = { dismissClickCalled = true }
                )
            }
        }

        assertThat(skipClickCalled).isFalse()
        assertThat(dismissClickCalled).isFalse()

        composeTestRule.onNodeWithText("Skip question?").assertIsDisplayed()
        composeTestRule.onNodeWithText("You have $userDiamonds diamonds, do you want to use $skipCost diamonds to skip this question?").assertIsDisplayed()

        composeTestRule.onNodeWithText("Close").assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Skip")
            .assertIsDisplayed()
            .performClick()


        assertThat(skipClickCalled).isTrue()
        assertThat(dismissClickCalled).isTrue()

        skipClickCalled = false
        dismissClickCalled = false

        composeTestRule.onNodeWithText("Close").performClick()
        assertThat(dismissClickCalled).isTrue()
        assertThat(skipClickCalled).isFalse()
    }

    @Test
    fun skipQuestionDialog_CannotSkip_ShowNoDiamondsDialog() {
        val userDiamonds = 3
        val skipCost = 5
        var skipClickCalled = false
        var dismissClickCalled = false

        composeTestRule.setContent {
            NewQuizTestTheme {
                SkipQuestionDialog(
                    userDiamonds = userDiamonds,
                    skipCost = skipCost,
                    onSkipClick = { skipClickCalled = true },
                    onDismissClick = { dismissClickCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithText("Skip question?").assertDoesNotExist()
        composeTestRule.onNodeWithText("No diamonds").assertIsDisplayed()
        composeTestRule.onNodeWithText("You don't have diamonds to skip this question!").assertIsDisplayed()

        composeTestRule.onNodeWithText("Skip").assertDoesNotExist()
        composeTestRule
            .onNodeWithText("Close")
            .assertIsDisplayed()
            .performClick()

        assertThat(dismissClickCalled).isTrue()
        assertThat(skipClickCalled).isFalse()
    }
}