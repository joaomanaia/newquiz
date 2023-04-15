package com.infinitepower.newquiz.comparison_quiz.ui.components

import android.net.Uri
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class ComparisonItemTest {
    @get:Rule
    val componentRule = createComposeRule()

    @Test
    fun comparisonItem_titleAndHelperValueDisplayed() {
        val title = "Item title"
        val helperValue = "Helper value"
        val helperValueState = HelperValueState.NORMAL
        var onClickCalled = false

        componentRule.setContent {
            ComparisonItem(
                title = title,
                image = Uri.EMPTY,
                helperValue = helperValue,
                helperContentAlignment = Alignment.BottomEnd,
                helperValueState = helperValueState,
                onClick = { onClickCalled = true },
                modifier = Modifier.testTag("ComparisonItem")
            )
        }

        componentRule
            .onNode(hasText(title) or hasText(helperValue))
            .assertExists()
            .assertIsDisplayed()

        componentRule
            .onNodeWithTag("ComparisonItem")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            // Click on the item
            .performClick()
            // Check that the item is still displayed and enabled after the click
            .assertIsDisplayed()
            .assertIsEnabled()

        assertThat(onClickCalled).isTrue()
    }

    @Test
    fun comparisonItem_titleDisplayed_andHelperValueNotDisplayedWhenStateHidden() {
        val title = "Item title"
        val helperValue = "Helper value"
        val helperValueState = HelperValueState.HIDDEN
        var onClickCalled = false

        componentRule.setContent {
            ComparisonItem(
                title = title,
                image = Uri.EMPTY,
                helperValue = helperValue,
                helperContentAlignment = Alignment.BottomEnd,
                helperValueState = helperValueState,
                onClick = { onClickCalled = true },
                modifier = Modifier.testTag("ComparisonItem")
            )
        }

        componentRule
            .onNodeWithText(title)
            .assertExists()
            .assertIsDisplayed()

        componentRule
            .onNodeWithText(helperValue)
            .assertDoesNotExist()

        componentRule
            .onNodeWithTag("ComparisonItem")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            // Click on the item
            .performClick()
            // Check that the item is still displayed and enabled after the click
            .assertIsDisplayed()
            .assertIsEnabled()

        assertThat(onClickCalled).isTrue()
    }
}