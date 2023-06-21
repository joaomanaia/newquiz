package com.infinitepower.newquiz.comparison_quiz.list.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core_test.compose.theme.NewQuizTestTheme
import com.infinitepower.newquiz.core_test.utils.setTestDeviceLocale
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class ComparisonModeComponentsTest {
    @get:Rule
    val componentRule = createComposeRule()

    @Test
    fun comparisonModeComponent_greater_shouldBeSelected() {
        componentRule.setContent {
            setTestDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponents(
                    modifier = Modifier.fillMaxWidth(),
                    selectedMode = ComparisonMode.GREATER
                )
            }
        }

        // Assert that the greater text and icon are displayed and content selected

        componentRule
            .onNodeWithText("Greater")
            .assertExists()
            .assertIsDisplayed()
            .assertIsSelected()

        componentRule
            .onNodeWithContentDescription("Icon of Greater")
            .assertExists()
            .assertIsDisplayed()

        // Assert that the lesser text and icon are displayed and content not selected

        componentRule
            .onNodeWithText("Lesser")
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotSelected()

        componentRule
            .onNodeWithContentDescription("Icon of Lesser")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun comparisonModeComponent_lesser_shouldBeSelected() {
        componentRule.setContent {
            setTestDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponents(
                    modifier = Modifier.fillMaxWidth(),
                    selectedMode = ComparisonMode.LESSER
                )
            }
        }

        // Assert that the greater text and icon are displayed and content selected

        componentRule
            .onNodeWithText("Greater")
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotSelected()

        componentRule
            .onNodeWithContentDescription("Icon of Greater")
            .assertExists()
            .assertIsDisplayed()

        // Assert that the lesser text and icon are displayed and content not selected

        componentRule
            .onNodeWithText("Lesser")
            .assertExists()
            .assertIsDisplayed()
            .assertIsSelected()

        componentRule
            .onNodeWithContentDescription("Icon of Lesser")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun row_hasCorrectModifiers() {
        componentRule.setContent {
            setTestDeviceLocale()

            NewQuizTestTheme {
                ComparisonModeComponents(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ComparisonModeComponents"),
                    selectedMode = ComparisonMode.GREATER
                )
            }
        }

        componentRule
            .onNodeWithTag("ComparisonModeComponents")
            .onChildren()
            .assertCountEquals(2)

        componentRule
            .onNodeWithTag("ComparisonModeComponents")
            .onChildren()
            .onFirst()
            .assertTextEquals("Greater")
            .assertIsDisplayed()
            .assertIsSelected()

        componentRule
            .onNodeWithTag("ComparisonModeComponents")
            .onChildren()
            .onLast()
            .assertTextEquals("Lesser")
            .assertIsDisplayed()
            .assertIsNotSelected()
    }

    @Test
    fun onModeClick_greater_onClick_when_otherModeIsSelected() {
        var selectedMode by mutableStateOf(ComparisonMode.GREATER)

        componentRule.setContent {
            ComparisonModeComponents(
                selectedMode = selectedMode,
                onModeClick = { mode -> selectedMode = mode }
            )
        }

        componentRule
            .onNodeWithText("Greater")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsSelected()

        // Click on the LESSER mode.
        componentRule
            .onNodeWithText("Lesser")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsNotSelected()
            .assertHasClickAction()
            .performClick() // Click on the mode that is not selected.
            .assertIsSelected() // This should select the mode.

        componentRule
            .onNodeWithText("Greater")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsNotSelected()

        // Verify that the onModeClick callback is called with the correct mode.
        assertThat(selectedMode).isNotNull()
        assertThat(selectedMode).isEqualTo(ComparisonMode.LESSER)
    }

    @Test
    fun onModeClick_greater_onClick_when_sameModeIsSelected() {
        var selectedMode by mutableStateOf(ComparisonMode.GREATER)

        componentRule.setContent {
            ComparisonModeComponents(
                selectedMode = selectedMode,
                onModeClick = { mode -> selectedMode = mode }
            )
        }

        componentRule
            .onNodeWithText("Lesser")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsNotSelected()

        // Click on the GREATER mode.
        componentRule
            .onNodeWithText("Greater")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsSelected()
            .assertHasClickAction()
            .performClick() // Click on the mode that is already selected.
            .assertIsSelected() // This should not do anything since the mode is already selected.

        componentRule
            .onNodeWithText("Lesser")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsNotSelected()

        // Verify that the onModeClick callback is called with the correct mode.
        assertThat(selectedMode).isNotNull()
        assertThat(selectedMode).isEqualTo(ComparisonMode.GREATER)
    }
}
