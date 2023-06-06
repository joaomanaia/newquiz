package com.infinitepower.newquiz.core.ui.components

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertRangeInfoEquals
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
    fun testInitialState() {
        val maxTime = 30.seconds
        val remainingTime = RemainingTime(30.seconds)

        // Render the RemainingTimeComponent
        composeTestRule.setContent {
            RemainingTimeComponent(
                remainingTime = remainingTime,
                maxTime = maxTime
            )
        }

        // Assert that the progress indicator is shown
        composeTestRule
            .onNodeWithTag(RemainingTimeComponentTestTags.PROGRESS_INDICATOR)
            .assertIsDisplayed()
            .assertRangeInfoEquals(
                ProgressBarRangeInfo(
                    current = 1f,
                    range = 0f..1f
                )
            )

        // Assert that the remaining time text is displayed without animation
        composeTestRule
            .onNodeWithText("30")
            .assertIsDisplayed()
    }

    @Test
    fun testWarningState() {
        val maxTime = 30.seconds
        val remainingTime = RemainingTime(5.seconds)

        // Render the RemainingTimeComponent
        composeTestRule.setContent {
            RemainingTimeComponent(
                remainingTime = remainingTime,
                maxTime = maxTime
            )
        }

        // Assert that the progress indicator is shown
        composeTestRule
            .onNodeWithTag(RemainingTimeComponentTestTags.PROGRESS_INDICATOR)
            .assertIsDisplayed()
            .assertRangeInfoEquals(
                ProgressBarRangeInfo(
                    current = remainingTime.getRemainingPercent(maxTime).toFloat(),
                    range = 0f..1f
                )
            )

        // Assert that the remaining time text is displayed without animation
        composeTestRule
            .onNodeWithText("5")
            .assertIsDisplayed()
    }
}