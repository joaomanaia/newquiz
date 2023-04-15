package com.infinitepower.newquiz.comparison_quiz.list.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core_test.compose.theme.NewQuizTestTheme
import com.infinitepower.newquiz.core_test.utils.setDeviceLocale
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class ComparisonModeComponentTest {
    @get:Rule
    val componentRule = createComposeRule()

    @Test
    fun comparisonModeComponent_greater_shouldDisplayTitleAndIcon() {
        componentRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponent(
                    selected = true,
                    mode = ComparisonModeByFirst.GREATER
                )
            }
        }

        // Assert that the title is displayed and has the correct text
        componentRule
            .onNodeWithText("Greater")
            .assertExists()
            .assertIsDisplayed()

        // Assert that the icon is displayed and has the correct content description
        componentRule
            .onNodeWithContentDescription("Icon of Greater")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun comparisonModeComponent_lesser_shouldDisplayTitleAndIcon() {
        componentRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponent(
                    selected = true,
                    mode = ComparisonModeByFirst.LESSER
                )
            }
        }

        // Assert that the title is displayed and has the correct text
        componentRule
            .onNodeWithText("Lesser")
            .assertExists()
            .assertIsDisplayed()

        // Assert that the icon is displayed and has the correct content description
        componentRule
            .onNodeWithContentDescription("Icon of Lesser")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun comparisonModeComponent_shouldInvokeOnClick_whenEnabled() {
        var clicked = false

        componentRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponent(
                    selected = true,
                    enabled = true,
                    mode = ComparisonModeByFirst.LESSER,
                    onClick = { clicked = true },
                    modifier = Modifier.testTag("ComparisonModeComponent")
                )
            }
        }

        // Click on the Composable
        componentRule
            .onNodeWithTag("ComparisonModeComponent")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()

        // Assert that the onClick callback was invoked
        assertThat(clicked).isTrue()
    }

    @Test
    fun comparisonModeComponent_shouldNotInvokeOnClick_whenNotEnabled() {
        var clicked = false

        componentRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponent(
                    selected = true,
                    enabled = false,
                    mode = ComparisonModeByFirst.LESSER,
                    onClick = { clicked = true },
                    modifier = Modifier.testTag("ComparisonModeComponent")
                )
            }
        }

        // Click on the Composable
        componentRule
            .onNodeWithTag("ComparisonModeComponent")
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .performClick()

        // Assert that the onClick callback was invoked
        assertThat(clicked).isFalse()
    }

    @Test
    fun comparisonModeComponent_shouldBeSelected() {
        componentRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponent(
                    selected = true,
                    mode = ComparisonModeByFirst.LESSER,
                    modifier = Modifier.testTag("ComparisonModeComponent")
                )
            }
        }

        componentRule
            .onNodeWithTag("ComparisonModeComponent")
            .assertIsDisplayed()
            .assertIsSelected()
    }

    @Test
    fun comparisonModeComponent_should_not_beSelected() {
        componentRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponent(
                    selected = false,
                    mode = ComparisonModeByFirst.LESSER,
                    modifier = Modifier.testTag("ComparisonModeComponent")
                )
            }
        }

        componentRule
            .onNodeWithTag("ComparisonModeComponent")
            .assertIsDisplayed()
            .assertIsNotSelected()
    }
}
