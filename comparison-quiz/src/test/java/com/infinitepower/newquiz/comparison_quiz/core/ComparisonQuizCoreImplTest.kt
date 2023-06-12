package com.infinitepower.newquiz.comparison_quiz.core

import android.net.Uri
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCurrentQuestion
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Unit tests for [ComparisonQuizCoreImpl].
 */
internal class ComparisonQuizCoreImplTest {
    private val comparisonQuizRepository = mockk<ComparisonQuizRepository>()
    private val userRepository = mockk<UserRepository>()

    private lateinit var comparisonQuizCoreImpl: ComparisonQuizCoreImpl

    @BeforeEach
    fun setup() {
        comparisonQuizCoreImpl = ComparisonQuizCoreImpl(
            comparisonQuizRepository = comparisonQuizRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun `initializeGame should emit correct data`() = runTest {
        val initialData = ComparisonQuizCore.InitializationData(
            category = ComparisonQuizCategory(
                id = "id",
                title = "title",
                description = "description",
                imageUrl = "imageUrl",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "greater",
                    less = "less"
                ),
                formatType = ComparisonQuizFormatType.Number,
                helperValueSuffix = "helperValueSuffix",
                dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                    text = "text",
                    logo = "logo"
                )
            ),
            comparisonMode = ComparisonMode.GREATER
        )

        // Mock Uri.parse() to return a mock Uri
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val expectedQuestions = listOf(
            ComparisonQuizItem(
                title = "Question 1",
                value = 1.0,
                imgUri = uriMock
            ),
            ComparisonQuizItem(
                title = "Question 2",
                value = 2.0,
                imgUri = uriMock
            ),
            ComparisonQuizItem(
                title = "Question 3",
                value = 3.0,
                imgUri = uriMock
            )
        )

        every {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns flowOf(Resource.Success(expectedQuestions))

        comparisonQuizCoreImpl.initializeGame(initialData)

        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        assertThat(quizData.comparisonMode).isEqualTo(initialData.comparisonMode)
        assertThat(quizData.questions).contains(expectedQuestions[2])

        val expectedCurrentQuestion = ComparisonQuizCurrentQuestion(
            questions = expectedQuestions[0] to expectedQuestions[1]
        )

        // verify current question
        assertThat(quizData.currentQuestion).isNotNull()
        assertThat(quizData.currentQuestion).isEqualTo(expectedCurrentQuestion)
    }

    @Test
    fun `onAnswerClicked should check the correct answer and move to the next question`() = runTest {
        val initialData = ComparisonQuizCore.InitializationData(
            category = ComparisonQuizCategory(
                id = "id",
                title = "title",
                description = "description",
                imageUrl = "imageUrl",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "greater",
                    less = "less"
                ),
                formatType = ComparisonQuizFormatType.Number,
                helperValueSuffix = "helperValueSuffix",
                dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                    text = "text",
                    logo = "logo"
                )
            ),
            comparisonMode = ComparisonMode.LESSER
        )

        // Mock Uri.parse() to return a mock Uri
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val expectedQuestions = listOf(
            ComparisonQuizItem(
                title = "Question 1",
                value = 1.0,
                imgUri = uriMock
            ),
            ComparisonQuizItem(
                title = "Question 2",
                value = 2.0,
                imgUri = uriMock
            ),
            ComparisonQuizItem(
                title = "Question 3",
                value = 3.0,
                imgUri = uriMock
            )
        )

        every {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns flowOf(Resource.Success(expectedQuestions))

        comparisonQuizCoreImpl.initializeGame(initialData)

        // verify current question
        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val currentQuestion = quizData.currentQuestion

        assertThat(quizData.comparisonMode).isEqualTo(initialData.comparisonMode)

        assertThat(quizData.isGameOver).isFalse()
        assertThat(currentQuestion).isNotNull()
        require(currentQuestion != null)

        assertThat(quizData.questions).hasSize(1)
        assertThat(quizData.questions).contains(expectedQuestions[2])

        assertThat(currentQuestion.questions.first).isEqualTo(expectedQuestions[0])
        assertThat(currentQuestion.questions.second).isEqualTo(expectedQuestions[1])

        // verify answer
        comparisonQuizCoreImpl.onAnswerClicked(currentQuestion.questions.first)

        val newQuizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val newCurrentQuestion = newQuizData.currentQuestion

        assertThat(newQuizData.isGameOver).isFalse()
        assertThat(newCurrentQuestion).isNotNull()
        require(newCurrentQuestion != null)

        assertThat(newQuizData.questions).isEmpty()

        assertThat(newCurrentQuestion.questions.first).isEqualTo(expectedQuestions[1])
        assertThat(newCurrentQuestion.questions.second).isEqualTo(expectedQuestions[2])
    }

    // Test when the user gets the answer wrong
    @Test
    fun `onAnswerClicked should check the wrong answer and move to the next question`() = runTest {
        val initialData = ComparisonQuizCore.InitializationData(
            category = ComparisonQuizCategory(
                id = "id",
                title = "title",
                description = "description",
                imageUrl = "imageUrl",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "greater",
                    less = "less"
                ),
                formatType = ComparisonQuizFormatType.Number,
                helperValueSuffix = "helperValueSuffix",
                dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                    text = "text",
                    logo = "logo"
                )
            ),
            comparisonMode = ComparisonMode.LESSER
        )

        // Mock Uri.parse() to return a mock Uri
        mockkStatic(Uri::class)
        val uriMock = mockk<Uri>()
        every { Uri.parse("test/path") } returns uriMock

        val expectedQuestions = listOf(
            ComparisonQuizItem(
                title = "Question 1",
                value = 1.0,
                imgUri = uriMock
            ),
            ComparisonQuizItem(
                title = "Question 2",
                value = 2.0,
                imgUri = uriMock
            ),
            ComparisonQuizItem(
                title = "Question 3",
                value = 3.0,
                imgUri = uriMock
            )
        )

        every {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns flowOf(Resource.Success(expectedQuestions))

        // Loads the initial data and starts the game
        // This will also set the current question
        // The answer will be wrong
        comparisonQuizCoreImpl.initializeGame(initialData)

        // Get the current data and question
        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val currentQuestion = quizData.currentQuestion

        // Check if comparison mode is correct
        assertThat(quizData.comparisonMode).isEqualTo(initialData.comparisonMode)

        // Check if current question is not null
        assertThat(currentQuestion).isNotNull()
        require(currentQuestion != null)

        assertThat(quizData.questions).hasSize(1)
        assertThat(quizData.questions).contains(expectedQuestions[2])

        assertThat(currentQuestion.questions.first).isEqualTo(expectedQuestions[0])
        assertThat(currentQuestion.questions.second).isEqualTo(expectedQuestions[1])

        // verify answer
        comparisonQuizCoreImpl.onAnswerClicked(currentQuestion.questions.second)

        val newQuizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val newCurrentQuestion = newQuizData.currentQuestion

        assertThat(newCurrentQuestion).isNull()
        assertThat(newQuizData.isGameOver).isTrue()
    }
}