package com.infinitepower.newquiz.wordle.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.model.wordle.WordleChar
import com.infinitepower.newquiz.model.wordle.WordleItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
internal class WordleRowComponentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun wordleComponent_emptyWordleItem_test() {
        val item = WordleItem.Empty

        composeTestRule.setContent {
            NewQuizTheme {
                Surface {
                    WordleComponent(
                        item = item,
                        onClick = {}
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithTag(WordleRowComponentTestingTags.WORDLE_COMPONENT_SURFACE)
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()
            .assertTextEquals(" ")
            .assertContentDescriptionEquals("Item empty")
    }

    @Test
    fun wordleComponent_noneWordleItem_test() {
        composeTestRule.setContent {
            val (item, setItem) = remember {
                mutableStateOf<WordleItem>(WordleItem.None(WordleChar('A')))
            }

            NewQuizTheme {
                Surface {
                    WordleComponent(
                        item = item,
                        onClick = { setItem(WordleItem.Empty) }
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithTag(WordleRowComponentTestingTags.WORDLE_COMPONENT_SURFACE)
            .assertExists()
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .assertTextEquals("A")
            .assertContentDescriptionEquals("Item A none")
            .performClick()
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()
            .assertTextEquals(" ")
            .assertContentDescriptionEquals("Item empty")
    }

    @Test
    fun wordleComponent_presentWordleItem_test() {
        composeTestRule.setContent {
            NewQuizTheme {
                Surface {
                    WordleComponent(
                        item = WordleItem.Present(WordleChar('A')),
                        onClick = {}
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithTag(WordleRowComponentTestingTags.WORDLE_COMPONENT_SURFACE)
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()
            .assertTextEquals("A")
            .assertContentDescriptionEquals("Item A present")
    }

    @Test
    fun wordleComponent_correctWordleItem_test() {
        composeTestRule.setContent {
            NewQuizTheme {
                Surface {
                    WordleComponent(
                        item = WordleItem.Correct(WordleChar('A')),
                        onClick = {}
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithTag(WordleRowComponentTestingTags.WORDLE_COMPONENT_SURFACE)
            .assertExists()
            .assertIsDisplayed()
            .assertIsNotEnabled()
            .assertHasClickAction()
            .assertTextEquals("A")
            .assertContentDescriptionEquals("Item A correct")
    }
}