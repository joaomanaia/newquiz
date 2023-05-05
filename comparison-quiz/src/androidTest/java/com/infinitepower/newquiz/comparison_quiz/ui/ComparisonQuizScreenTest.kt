package com.infinitepower.newquiz.comparison_quiz.ui

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.comparison_quiz.core.ComparisonQuizCoreImpl
import com.infinitepower.newquiz.comparison_quiz.data.comparison_quiz.FakeComparisonQuizRepositoryImpl
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core_test.compose.theme.NewQuizTestTheme
import com.infinitepower.newquiz.core_test.utils.setDeviceLocale
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for [ComparisonQuizScreen].
 */
@RunWith(AndroidJUnit4::class)
@OptIn(
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalTestApi::class
)
internal class ComparisonQuizScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var comparisonQuizRepository: ComparisonQuizRepository

    private lateinit var comparisonQuizCore: ComparisonQuizCore

    private lateinit var viewModel: ComparisonQuizViewModel

    private val comparisonMode by lazy { ComparisonModeByFirst.GREATER }

    private val category by lazy {
        ComparisonQuizCategory(
            id = "numbers",
            title = "Numbers",
            description = "Numbers description",
            imageUrl = "",
            questionDescription = ComparisonQuizCategory.QuestionDescription(
                greater = "Which number is greater?",
                less = "Which number is lesser?",
            ),
            formatType = ComparisonQuizFormatType.Number,
            dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                text = "NewQuiz API",
                logo = ""
            )
        )
    }

    @Before
    fun setUp() {
        comparisonQuizRepository = FakeComparisonQuizRepositoryImpl(
            initialQuestions = listOf(
                ComparisonQuizItem(
                    title = "Question 1",
                    value = 1.0,
                    imgUri = Uri.EMPTY
                ),
                ComparisonQuizItem(
                    title = "Question 2",
                    value = 2.0,
                    imgUri = Uri.EMPTY
                ),
                ComparisonQuizItem(
                    title = "Question 3",
                    value = 3.0,
                    imgUri = Uri.EMPTY
                ),
            ),
            initialCategories = listOf(category)
        )

        comparisonQuizCore = ComparisonQuizCoreImpl(comparisonQuizRepository)

        viewModel = ComparisonQuizViewModel(
            comparisonQuizCore = comparisonQuizCore,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    ComparisonQuizListScreenNavArg::comparisonMode.name to comparisonMode,
                    ComparisonQuizListScreenNavArg::category.name to category
                )
            ),
            comparisonQuizRepository = comparisonQuizRepository
        )

        composeTestRule.setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = composeTestRule.activity)

            NewQuizTestTheme {
                setDeviceLocale()

                ComparisonQuizScreen(
                    windowSizeClass = windowSizeClass,
                    navigator = EmptyDestinationsNavigator,
                    viewModel = viewModel
                )
            }
        }
    }

    @Test
    fun comparisonQuizScreen_testComponentsDisplayed() {
        val questionDescription = category.getQuestionDescription(comparisonMode)

        composeTestRule.waitUntilAtLeastOneExists(
            matcher = hasText(questionDescription),
            timeoutMillis = 5000
        )

        // Check if the question description is displayed
        composeTestRule
            .onNodeWithText(questionDescription)
            .assertIsDisplayed()

        // Check if the first item is displayed
        composeTestRule
            .onNodeWithText("Question 1")
            .assertIsDisplayed()

        // Check if first item helper text is displayed
        composeTestRule
            .onNodeWithText("1")
            .assertIsDisplayed()

        // Check if the second item is displayed
        composeTestRule
            .onNodeWithText("Question 2")
            .assertIsDisplayed()

        // Check if second item helper text is not displayed
        composeTestRule
            .onNodeWithText("2")
            .assertDoesNotExist()

        // Check if data source attribution is displayed
        composeTestRule
            .onNodeWithText("NewQuiz API")
            .assertIsDisplayed()

        // Check if current position is displayed
        composeTestRule
            .onNodeWithText("Position: 1")
            .assertIsDisplayed()

        // Check if highest position is displayed
        composeTestRule
            .onNodeWithText("Highest: 1")
            .assertIsDisplayed()
    }

    @Test
    fun comparisonQuizScreen_testWrongAnswerClick() {
        // Click on the first item (wrong answer)
        // The value of the first item is 1.0
        // The value of the second item is 2.0
        composeTestRule
            .onNodeWithText("Question 1")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()
            // When the wrong answer is clicked should end the game
            .assertDoesNotExist()

        // Check if the game is over
        assertThat(viewModel.uiState.value.isGameOver).isTrue()

        // Check if the game over screen is displayed
        composeTestRule
            .onNodeWithText("Game Over")
            .assertIsDisplayed()
    }

    @Test
    fun comparisonQuizScreen_testCorrectAnswerClick() {
        // Click on the second item (correct answer)
        // The value of the first item is 1.0
        // The value of the second item is 2.0
        composeTestRule
            .onNodeWithText("Question 2")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertHasClickAction()
            .performClick()
            // When the correct answer is clicked should not end the game
            .assertIsDisplayed()
            .assertHasClickAction()

        // Check if the game is not over
        assertThat(viewModel.uiState.value.isGameOver).isFalse()

        // Check if the game over screen is not displayed
        composeTestRule
            .onNodeWithText("Game Over")
            .assertDoesNotExist()

        // Now the first item should be the second item
        // and the second item should be a new item
        composeTestRule
            .onNodeWithText("Question 3")
            .assertIsDisplayed()
            .assertHasClickAction()
    }
}