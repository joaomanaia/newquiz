package com.infinitepower.newquiz.compose.ui.quiz

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.infinitepower.newquiz.compose.di.AppModule
import com.infinitepower.newquiz.compose.domain.use_case.question.GetQuestions
import com.infinitepower.newquiz.compose.model.quiz.QuizStep
import com.infinitepower.newquiz.compose.ui.theme.NewQuizTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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

    @Inject
    lateinit var getQuestions: GetQuestions

    lateinit var viewModel: QuizViewModel

    @BeforeTest
    fun setUp() {
        hiltRule.inject()

        viewModel = QuizViewModel(
            getQuestions = getQuestions,
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "quizOptions" to QuizOption.QuickQuiz.id
                )
            )
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