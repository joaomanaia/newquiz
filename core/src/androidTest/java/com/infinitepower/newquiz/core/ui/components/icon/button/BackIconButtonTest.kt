package com.infinitepower.newquiz.core.ui.components.icon.button

import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.infinitepower.newquiz.core.assertMatchesGolden
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class BackIconButtonTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun backIconButton_shouldRenderCorrectly() {
        var clicked = false

        composeTestRule.setContent {
            Surface {
                BackIconButton(
                    onClick = { clicked = true },
                    modifier = Modifier.testTag("BackButton")
                )
            }
        }

        // Assert that an IconButton and an Icon are rendered with the correct attributes
        composeTestRule
            .onNodeWithTag("BackButton")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()

        assert(clicked)
    }

    @Test
    fun test_backIconButton_screenshot() {
        composeTestRule.setContent {
            Surface {
                BackIconButton(
                    onClick = {},
                    modifier = Modifier.testTag("BackButton")
                )
            }
        }

        composeTestRule
            .onNodeWithTag("BackButton")
            .assertMatchesGolden("back_button", "buttons")
    }
}
