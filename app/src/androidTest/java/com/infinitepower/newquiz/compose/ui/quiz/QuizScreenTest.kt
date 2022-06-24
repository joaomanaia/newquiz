package com.infinitepower.newquiz.compose.ui.quiz

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import androidx.work.WorkManager
import com.infinitepower.newquiz.compose.core.util.quiz.QuizXPUtil
import com.infinitepower.newquiz.compose.data.remote.auth.user.AuthUserApi
import com.infinitepower.newquiz.compose.di.AppModule
import com.infinitepower.newquiz.compose.domain.use_case.question.GetQuestions
import com.infinitepower.newquiz.compose.ui.theme.NewQuizTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Rule
import javax.inject.Inject
import kotlin.test.BeforeTest
import kotlin.test.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class QuizScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Inject lateinit var getQuestions: GetQuestions

    @Inject lateinit var workManager: WorkManager

    @Inject lateinit var authUserApi: AuthUserApi

    lateinit var viewModel: QuizViewModel

    @BeforeTest
    fun setUp() {
        hiltRule.inject()

        viewModel = QuizViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "quizOptions" to com.infinitepower.newquiz.compose.quiz_presentation.QuizType.QuickQuiz.id
                )
            ),
            xpUtil = QuizXPUtil(),
            workManager = workManager,
            authUserApi = authUserApi
        )

        composeTestRule.setContent {
            NewQuizTheme {
                val navController = rememberNavController()
                QuizScreen(navController = navController, quizViewModel = viewModel)
            }
        }
    }

    @Test
    fun testVisibilityFields() {
        composeTestRule.onNodeWithTag(QuizScreenTestTags.TEXT_TIME_LEFT).assertIsDisplayed()
    }
}