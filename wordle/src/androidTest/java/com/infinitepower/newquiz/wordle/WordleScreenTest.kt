package com.infinitepower.newquiz.wordle

import androidx.compose.material3.Surface
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.analytics.logging.wordle.LocalWordleLoggingAnalyticsImpl
import com.infinitepower.newquiz.core.analytics.logging.wordle.WordleLoggingAnalytics
import com.infinitepower.newquiz.core.theme.NewQuizTheme
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.wordle.data.repository.wordle.daily.FakeDailyWordleRepository
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class WordleScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: WordleScreenViewModel

    @Inject lateinit var wordleRepository: WordleRepository
    @Inject lateinit var workManager: WorkManager

    @Before
    fun setup() {
        hiltRule.inject()

        composeRule.setContent {
            NewQuizTheme {
                Surface {
                    savedStateHandle = SavedStateHandle(mapOf(WordleScreenNavArgs::rowLimit.name to 3))

                    val localAnalytics: WordleLoggingAnalytics = LocalWordleLoggingAnalyticsImpl()
                    viewModel = WordleScreenViewModel(wordleRepository, savedStateHandle, localAnalytics, workManager)

                    WordleScreen(wordleScreenViewModel = viewModel, navigator = EmptyDestinationsNavigator)
                }
            }
        }
    }

    @Test
    fun wordleScreen_basicControls() {
        composeRule.waitUntil {
            !viewModel.uiState.value.loading
        }

        composeRule.onNodeWithTag(WordleScreenTestTags.VERIFY_FAB).assertDoesNotExist()

        composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .onChildren()
            .assertCountEquals(WordleScreenUiState.ALL_LETTERS.length)
            .assertAll(isEnabled())
            .assertAll(hasClickAction())

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(1)
            .onFirst()
            .onChildren()
            .assertAll(hasText(" "))
            .assertAll(hasContentDescription("Item empty"))

        composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .onChildren()
            .onFirst()
            .performClick()

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .onFirst()
            .onChildren()
            .filter(hasText(" "))
            .assertCountEquals(3)

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .onFirst()
            .onChildren()
            .onFirst()
            .assertTextEquals("Q")
            .assertContentDescriptionEquals("Item Q none")
            .performClick()
            .assertTextEquals(" ")
            .assertContentDescriptionEquals("Item empty")
    }

    @Test
    fun wordleScreen_verifyWord() {
        composeRule.waitUntil {
            !viewModel.uiState.value.loading
        }

        composeRule.onNodeWithTag(WordleScreenTestTags.VERIFY_FAB).assertDoesNotExist()

        composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .onChildren()
            .assertCountEquals(WordleScreenUiState.ALL_LETTERS.length)
            .assertAll(isEnabled())
            .assertAll(hasClickAction())

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(1)
            .onFirst()
            .onChildren()
            .assertAll(hasText(" "))
            .assertAll(hasContentDescription("Item empty"))

        val keyboardSemantics = composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .onChildren()

        keyboardSemantics[WordleScreenUiState.ALL_LETTERS.indexOf('A')].performClick()
        keyboardSemantics[WordleScreenUiState.ALL_LETTERS.indexOf('E')].performClick()
        keyboardSemantics[WordleScreenUiState.ALL_LETTERS.indexOf('T')].performClick()
        keyboardSemantics[WordleScreenUiState.ALL_LETTERS.indexOf('T')].performClick()

        composeRule
            .onNodeWithTag(WordleScreenTestTags.VERIFY_FAB)
            .assertIsDisplayed()
            .performClick()

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .onFirst()
            .onChildren()
            .filter(hasText(" "))
            .assertCountEquals(0)

        val rowFirstChildrenSemantics = composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .onFirst()
            .onChildren()

        rowFirstChildrenSemantics
            .onFirst()
            .assertTextEquals("A")
            .assertContentDescriptionEquals("Item A none")

        rowFirstChildrenSemantics[1]
            .assertTextEquals("E")
            .assertContentDescriptionEquals("Item E correct")

        rowFirstChildrenSemantics[2]
            .assertTextEquals("T")
            .assertContentDescriptionEquals("Item T present")

        rowFirstChildrenSemantics[3]
            .assertTextEquals("T")
            .assertContentDescriptionEquals("Item T correct")

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(2)
            .onLast()
            .onChildren()
            .assertAll(hasText(" "))
            .assertAll(hasContentDescription("Item empty"))

        composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .onChildren()
            .assertCountEquals(WordleScreenUiState.ALL_LETTERS.length)
            .filter(hasText("A"))
            .assertAll(isNotEnabled())

        composeRule.onNodeWithTag(WordleScreenTestTags.VERIFY_FAB).assertDoesNotExist()
    }

    @Test
    fun wordleScreen_rowLimit() {
        composeRule.waitUntil {
            !viewModel.uiState.value.loading
        }

        composeRule.onNodeWithTag(WordleScreenTestTags.VERIFY_FAB).assertDoesNotExist()

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(1)
            .onFirst()
            .onChildren()
            .assertAll(hasText(" "))
            .assertAll(hasContentDescription("Item empty"))

        clickFirstKeyWordTimes()

        composeRule.onNodeWithTag(WordleScreenTestTags.VERIFY_FAB)
            .assertIsDisplayed()
            .performClick()
            .assertDoesNotExist()

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(2)

        assert(!viewModel.uiState.value.isGamedEnded)

        clickFirstKeyWordTimes()

        composeRule.onNodeWithTag(WordleScreenTestTags.VERIFY_FAB)
            .assertIsDisplayed()
            .performClick()
            .assertDoesNotExist()

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(3)

        //assert(!viewModel.uiState.value.isGamedEnded)

        clickFirstKeyWordTimes()

        composeRule.onNodeWithTag(WordleScreenTestTags.VERIFY_FAB)
            .assertIsDisplayed()
            .performClick()
            .assertDoesNotExist()

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(3)

        composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .assertDoesNotExist()

        assert(viewModel.uiState.value.isGamedEnded)
    }

    @Test
    fun wordleScreen_correctWord() {
        composeRule.waitUntil {
            !viewModel.uiState.value.loading
        }

        composeRule.onNodeWithTag(WordleScreenTestTags.VERIFY_FAB).assertDoesNotExist()

        composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .onChildren()
            .assertCountEquals(WordleScreenUiState.ALL_LETTERS.length)
            .assertAll(isEnabled())
            .assertAll(hasClickAction())

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(1)
            .onFirst()
            .onChildren()
            .assertAll(hasText(" "))
            .assertAll(hasContentDescription("Item empty"))

        val keyboardSemantics = composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .onChildren()

        keyboardSemantics[WordleScreenUiState.ALL_LETTERS.indexOf('T')].performClick()
        keyboardSemantics[WordleScreenUiState.ALL_LETTERS.indexOf('E')].performClick()
        keyboardSemantics[WordleScreenUiState.ALL_LETTERS.indexOf('S')].performClick()
        keyboardSemantics[WordleScreenUiState.ALL_LETTERS.indexOf('T')].performClick()

        composeRule
            .onNodeWithTag(WordleScreenTestTags.VERIFY_FAB)
            .performClick()
            .assertDoesNotExist()

        composeRule
            .onAllNodesWithTag(WordleScreenTestTags.WORDLE_ROW)
            .assertCountEquals(1)

        composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .assertDoesNotExist()
    }

    private fun clickFirstKeyWordTimes() {
        val tKeyIndex = WordleScreenUiState.ALL_LETTERS.indexOf('T')

        val firstKey = composeRule
            .onNodeWithTag(WordleScreenTestTags.KEYBOARD)
            .onChildren()[tKeyIndex]

        val wordLength = viewModel.uiState.value.word?.length ?: -1
        assert(wordLength > 0)

        repeat(wordLength) { firstKey.performClick() }
    }
}