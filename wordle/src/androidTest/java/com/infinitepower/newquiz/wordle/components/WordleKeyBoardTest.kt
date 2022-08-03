package com.infinitepower.newquiz.wordle.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class WordleKeyBoardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun wordleKeyBoardItem_test() {
        composeTestRule.setContent {
            val (enabled, setEnabled) = remember {
                mutableStateOf(true)
            }

            NewQuizTheme {
                Surface {
                    WordleKeyBoardItem(
                        key = 'A',
                        enabled = enabled,
                        onClick = { setEnabled(false) }
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithText("A")
            .assertExists()
            .assertHeightIsAtLeast(35.dp)
            .assertWidthIsAtLeast(35.dp)
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()

        composeTestRule
            .onNodeWithText("A")
            .assertExists()
            .assertHeightIsAtLeast(35.dp)
            .assertWidthIsAtLeast(35.dp)
            .assertIsNotEnabled()
    }
}