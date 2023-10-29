package com.infinitepower.newquiz.core.ui.components.category

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.infinitepower.newquiz.core.testing.utils.setTestContent
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

/**
 * Tests for [CategoryConnectionInfoBadge] component.
 */
@SmallTest
@RunWith(AndroidJUnit4::class)
internal class CategoryConnectionInfoBadgeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_requireConnection_and_noShowTextByDefault() {
        composeTestRule.setTestContent {
            CategoryConnectionInfoBadge(
                modifier = Modifier.testTag(COMPONENT_TAG),
                requireConnection = true,
                showTextByDefault = false
            )
        }

        composeTestRule
            .onNodeWithText("Requires internet connection")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription("Requires internet connection")
            .assertIsDisplayed()
            .performClick() // Expand the badge to show the text

        composeTestRule
            .onNodeWithText("Requires internet connection")
            .assertIsDisplayed()
            .performClick() // Collapse the badge to hide the text
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription("Requires internet connection")
            .assertIsDisplayed()
    }

    @Test
    fun test_requireConnection_and_showTextByDefault() {
        composeTestRule.setTestContent {
            CategoryConnectionInfoBadge(
                modifier = Modifier.testTag(COMPONENT_TAG),
                requireConnection = true,
                showTextByDefault = true
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Requires internet connection")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Requires internet connection")
            .assertIsDisplayed()
            .performClick() // Collapse the badge to hide the text
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription("Requires internet connection")
            .assertIsDisplayed()
    }

    companion object {
        private const val COMPONENT_TAG = "offline_category_badge"
    }
}