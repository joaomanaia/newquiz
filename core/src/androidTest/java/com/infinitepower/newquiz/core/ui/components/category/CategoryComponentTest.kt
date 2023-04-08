package com.infinitepower.newquiz.core.ui.components.category

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.infinitepower.newquiz.core_test.compose.theme.NewQuizTestTheme
import com.infinitepower.newquiz.core_test.utils.setDeviceLocale
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class CategoryComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun categoryComponent_titleAndImageDisplayed() {
        val title = "Test Title"
        val imageUrl = "https://testimage.com/image.jpg"

        composeTestRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                CategoryComponent(
                    modifier = Modifier.testTag("CategoryComponent"),
                    title = title,
                    imageUrl = imageUrl,
                    enabled = true
                )
            }
        }

        composeTestRule
            .onNodeWithTag("CategoryComponent")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()

        composeTestRule
            .onNodeWithContentDescription("Image category of $title")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }

    @Test
    fun categoryComponent_onClick() {
        var clicked = false

        val title = "Test Title"
        val imageUrl = "https://testimage.com/image.jpg"

        composeTestRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                CategoryComponent(
                    modifier = Modifier.testTag("CategoryComponent"),
                    title = title,
                    imageUrl = imageUrl,
                    enabled = true,
                    onClick = { clicked = true }
                )
            }
        }
        composeTestRule
            .onNodeWithTag("CategoryComponent")
            .assertHasClickAction()
            .performClick()

        assert(clicked)
    }

    @Test
    fun categoryComponent_disabled() {
        val title = "Test Title"
        val imageUrl = "https://testimage.com/image.jpg"

        composeTestRule.setContent {
            setDeviceLocale()

            NewQuizTestTheme {
                CategoryComponent(
                    modifier = Modifier.testTag("CategoryComponent"),
                    title = title,
                    imageUrl = imageUrl,
                    enabled = false
                )
            }
        }

        composeTestRule
            .onNodeWithTag("CategoryComponent")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }
}