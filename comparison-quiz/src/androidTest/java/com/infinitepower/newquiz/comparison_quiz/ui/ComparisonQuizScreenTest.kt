package com.infinitepower.newquiz.comparison_quiz.ui

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
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.comparison_quiz.core.ComparisonQuizCoreImpl
import com.infinitepower.newquiz.comparison_quiz.data.comparison_quiz.FakeComparisonQuizRepositoryImpl
import com.infinitepower.newquiz.core.analytics.LocalDebugAnalyticsHelper
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizHelperValueState
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.toUiText
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URI

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

    private val authUserRepository = mockk<AuthUserRepository>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val remoteConfig = mockk<RemoteConfig>(relaxed = true)
    private val recentCategoriesRepository = mockk<RecentCategoriesRepository>(relaxed = true)

    private lateinit var comparisonQuizCore: ComparisonQuizCore

    private lateinit var viewModel: ComparisonQuizViewModel

    private lateinit var workManager: WorkManager

    private val comparisonMode by lazy { ComparisonMode.GREATER }

    private val category by lazy {
        ComparisonQuizCategory(
            id = "numbers",
            name = "Numbers".toUiText(),
            description = "Numbers description",
            image = "",
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

    private val firstItemHelperValueState = ComparisonQuizHelperValueState.HIDDEN

    @Before
    fun setUp() {
        comparisonQuizRepository = FakeComparisonQuizRepositoryImpl(
            initialQuestions = listOf(
                ComparisonQuizItem(
                    title = "Question 1",
                    value = 1.0,
                    imgUri = URI("")
                ),
                ComparisonQuizItem(
                    title = "Question 2",
                    value = 2.0,
                    imgUri = URI("")
                ),
                ComparisonQuizItem(
                    title = "Question 3",
                    value = 3.0,
                    imgUri = URI("")
                ),
            ),
            initialCategories = listOf(category)
        )

        every { authUserRepository.isSignedIn } returns false

        every {
            remoteConfig.getString("comparison_quiz_first_item_helper_value")
        } returns firstItemHelperValueState.name

        comparisonQuizCore = ComparisonQuizCoreImpl(
            comparisonQuizRepository = comparisonQuizRepository,
            userRepository = userRepository,
            remoteConfig = remoteConfig,
            analyticsHelper = LocalDebugAnalyticsHelper()
        )

        val context = InstrumentationRegistry.getInstrumentation().context
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

        workManager = WorkManager.getInstance(context)

        viewModel = ComparisonQuizViewModel(
            comparisonQuizCore = comparisonQuizCore,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    ComparisonQuizListScreenNavArg::comparisonMode.name to comparisonMode,
                    ComparisonQuizListScreenNavArg::category.name to category.toEntity()
                )
            ),
            comparisonQuizRepository = comparisonQuizRepository,
            workManager = workManager,
            authUserRepository = authUserRepository,
            userRepository = userRepository,
            recentCategoriesRepository = recentCategoriesRepository,
            analyticsHelper = LocalDebugAnalyticsHelper()
        )

        composeTestRule.setContent {
            val windowSizeClass = calculateWindowSizeClass(activity = composeTestRule.activity)

            com.infinitepower.newquiz.core.testing.ui.theme.NewQuizTestTheme {
                com.infinitepower.newquiz.core.testing.utils.setTestDeviceLocale()

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
            .apply {
                if (firstItemHelperValueState == ComparisonQuizHelperValueState.HIDDEN) {
                    assertDoesNotExist()
                } else {
                    assertIsDisplayed()
                }
            }

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