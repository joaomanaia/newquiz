package com.infinitepower.newquiz.multi_choice_quiz

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.isNotSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core_test.utils.setTestContent
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.domain.use_case.question.GetRandomMultiChoiceQuestionUseCase
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test

/**
 * Tests for [MultiChoiceQuizScreen].
 */
@RunWith(AndroidJUnit4::class)
@OptIn(
    ExperimentalTestApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)
internal class MultiChoiceQuizScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var workManager: WorkManager
    private lateinit var getRandomQuestionUseCase: GetRandomMultiChoiceQuestionUseCase

    private val authUserRepository = mockk<AuthUserRepository>()
    private val settingsDataStoreManager = mockk<DataStoreManager>()

    @BeforeTest
    fun setUp() {
        WorkManagerTestInitHelper.initializeTestWorkManager(composeTestRule.activity)
        workManager = WorkManager.getInstance(composeTestRule.activity)

        val questionsRepository = mockk<MultiChoiceQuestionRepository>()

        coEvery {
            questionsRepository.getRandomQuestions(any(), any(), any())
        } returns List(5) {
            getBasicMultiChoiceQuestion(
                correctAns = 0
            )
        }

        getRandomQuestionUseCase = GetRandomMultiChoiceQuestionUseCase(
            normalQuestionRepository = questionsRepository,
            flagQuizRepository = mockk(),
            logoQuizRepository = mockk(),
            guessMathSolutionRepository = mockk(),
            numberTriviaQuestionRepository = mockk(),
            countryCapitalFlagsQuizRepository = mockk()
        )
    }

    @AfterTest
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    @Ignore
    fun testMultiChoiceQuizScreen() {
        every { authUserRepository.isSignedIn } returns true

        coEvery {
            settingsDataStoreManager.getPreference(SettingsCommon.MultiChoiceQuizQuestionsSize)
        } returns 5

        coEvery {
            settingsDataStoreManager.getPreference(SettingsCommon.MultiChoiceQuiz.TranslationEnabled)
        } returns false

        composeTestRule.setTestContent {
            val windowSizeClass = calculateWindowSizeClass(composeTestRule.activity)

            MultiChoiceQuizScreen(
                navigator = EmptyDestinationsNavigator,
                windowSizeClass = windowSizeClass,
                viewModel = QuizScreenViewModel(
                    getRandomQuestionUseCase = getRandomQuestionUseCase,
                    settingsDataStoreManager = settingsDataStoreManager,
                    savedQuestionsRepository = mockk(),
                    recentCategoriesRepository = mockk(),
                    savedStateHandle = SavedStateHandle(
                        mapOf(
                            "category" to MultiChoiceBaseCategory.Random
                        )
                    ),
                    translationUtil = mockk(relaxed = true),
                    workManager = workManager,
                    userRepository = mockk(),
                    authUserRepository = authUserRepository,
                    isQuestionSavedUseCase = mockk(relaxed = true),
                    multiChoiceQuizLoggingAnalytics = mockk(relaxed = true),
                    coreLoggingAnalytics = mockk(relaxed = true),
                    mazeLoggingAnalytics = mockk(relaxed = true),
                )
            )
        }

        composeTestRule.waitUntilDoesNotExist(hasText("Loading..."))

        val random = Random(1)

        // Test quiz step row
        composeTestRule
            .onNodeWithContentDescription("Quiz steps container")
            .assertIsDisplayed()
            .onChildren()
            .assertCountEquals(5)
            .assertAll(isNotEnabled())
            .onFirst()
            .assertTextEquals("1")

        for (questionNumber in 1..5) {
            composeTestRule
                .onNodeWithText("Question $questionNumber/5")
                .assertIsDisplayed()

            // Test question description
            composeTestRule.onNodeWithText("Question description").assertIsDisplayed()

            // Test question answers
            composeTestRule
                .onNodeWithContentDescription("Answers container")
                .assertIsDisplayed()
                .onChildren()
                .assertCountEquals(4)
                .assertAll(isEnabled() and isNotSelected())

            // Test verify button
            composeTestRule.onNodeWithText("Verify").assertDoesNotExist()

            val randomAnswer = (0..3).random(random)
            val correctAnswer = randomAnswer == 0
            val randomAnswerText = "Answer ${randomAnswer + 1}"

            // Click on random answer
            composeTestRule
                .onNodeWithText(randomAnswerText)
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick()
                .assertIsEnabled()
                .assertIsSelected()

            // Check if verify button is displayed after selecting an answer
            composeTestRule
                .onNode(hasText("Verify") or hasContentDescription("Verify"))
                .assertIsDisplayed()
                .assertIsEnabled()
                .performClick() // Click on verify button
                .assertDoesNotExist() // Check if verify button is not displayed after clicking on it

            // Check if correct answer is displayed in the steps
            composeTestRule
                .onNodeWithContentDescription("Quiz steps container")
                .assertIsDisplayed()
                .onChildren()
                .assertAll(isNotEnabled())
                .apply {
                    val description = if (correctAnswer) "Question $questionNumber - Correct" else "Question $questionNumber - Incorrect"

                    this[questionNumber - 1]
                        .assertContentDescriptionEquals(description)
                        .assertIsDisplayed()
                }
        }
    }
}
