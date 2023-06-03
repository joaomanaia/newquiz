package com.infinitepower.newquiz.core.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.infinitepower.newquiz.model.RemainingTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration.Companion.seconds

@SmallTest
@RunWith(AndroidJUnit4::class)
class RemainingTimeComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testWithRemainingTime() {
        val maxTime = 72.seconds

        val remainingTime = RemainingTime(maxTime)
        val showProgressIndicator = true

        composeTestRule.setContent {
            RemainingTimeComponent(
                remainingTime = remainingTime,
                maxTimeMillis = maxTime.inWholeMilliseconds,
                showProgressIndicator = showProgressIndicator
            )
        }

        composeTestRule
            .onNodeWithText("1:12")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(RemainingTimeComponentTestTags.PROGRESS_INDICATOR)
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testWithoutProgressIndicator() {
        val remainingTime = RemainingTime(60.seconds)
        val maxTimeMillis = 60000L
        val showProgressIndicator = false

        composeTestRule.setContent {
            RemainingTimeComponent(
                remainingTime = remainingTime,
                maxTimeMillis = maxTimeMillis,
                showProgressIndicator = showProgressIndicator
            )
        }

        composeTestRule
            .onNodeWithTag(RemainingTimeComponentTestTags.PROGRESS_INDICATOR)
            .assertDoesNotExist()
    }
}