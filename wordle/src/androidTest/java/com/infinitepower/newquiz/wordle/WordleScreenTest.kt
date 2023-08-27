package com.infinitepower.newquiz.wordle

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.infinitepower.newquiz.core.analytics.LocalDebugAnalyticsHelper
import com.infinitepower.newquiz.core_test.utils.setTestContent
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.*
import javax.inject.Inject
import kotlin.test.BeforeTest
import kotlin.test.Test

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class WordleScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: WordleScreenViewModel

    @Inject
    lateinit var wordleRepository: WordleRepository


    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private lateinit var workManager: WorkManager

    @BeforeTest
    fun setup() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().context

        val workConfig = androidx.work.Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, workConfig)
        workManager = WorkManager.getInstance(context)

        composeRule.setTestContent {
            savedStateHandle = SavedStateHandle(
                mapOf(
                    WordleScreenNavArgs::rowLimit.name to 3,
                    WordleScreenNavArgs::word.name to "TEST"
                )
            )

            viewModel = WordleScreenViewModel(
                wordleRepository = wordleRepository,
                savedStateHandle = savedStateHandle,
                workManager = workManager,
                analyticsHelper = LocalDebugAnalyticsHelper()
            )

            val windowSizeClass = calculateWindowSizeClass(activity = composeRule.activity)

            CompositionLocalProvider(LocalInspectionMode provides true) {
                WordleScreen(
                    wordleScreenViewModel = viewModel,
                    navigator = EmptyDestinationsNavigator,
                    windowSizeClass = windowSizeClass
                )
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