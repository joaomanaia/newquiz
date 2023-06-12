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
import com.infinitepower.newquiz.model.config.RemoteConfigApi
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Unit tests for [ComparisonQuizCoreImpl].
 */
internal class ComparisonQuizCoreImplTest {
    private val comparisonQuizRepository = mockk<ComparisonQuizRepository>()
    private val userRepository = mockk<UserRepository>()
    private val remoteConfigApi = mockk<RemoteConfigApi>()

    private lateinit var comparisonQuizCoreImpl: ComparisonQuizCoreImpl

    @BeforeEach
    fun setup() {
        comparisonQuizCoreImpl = ComparisonQuizCoreImpl(
            comparisonQuizRepository = comparisonQuizRepository,
            userRepository = userRepository,
            remoteConfigApi = remoteConfigApi
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

    @Test
    fun `canSkip should return true when user has enough diamonds`() = runTest {
        val skipCost = 10u
        val userDiamonds = 15

        every { remoteConfigApi.getLong("comparison_quiz_skip_cost") } returns skipCost.toLong()
        coEvery { userRepository.getLocalUserDiamonds() } returns userDiamonds

        val result = comparisonQuizCoreImpl.canSkip()

        // Verify interactions
        verify { remoteConfigApi.getLong("comparison_quiz_skip_cost") }
        coVerify { userRepository.getLocalUserDiamonds() }
        confirmVerified(remoteConfigApi, userRepository)

        // Verify result
        assertThat(result).isTrue()
    }

    @Test
    fun `canSkip should return false when user doesn't have enough diamonds`() = runTest {
        val skipCost = 10u
        val userDiamonds = 5

        every { remoteConfigApi.getLong("comparison_quiz_skip_cost") } returns skipCost.toLong()
        coEvery { userRepository.getLocalUserDiamonds() } returns userDiamonds

        val result = comparisonQuizCoreImpl.canSkip()

        // Verify interactions
        verify { remoteConfigApi.getLong("comparison_quiz_skip_cost") }
        coVerify { userRepository.getLocalUserDiamonds() }
        confirmVerified(remoteConfigApi, userRepository)

        // Verify result
        assertThat(result).isFalse()
    }

    @Test
    fun `skip() should deduct diamonds and update quiz data`() = runTest {
        val skipCost = 1u
        val userDiamonds = 10

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

        every { remoteConfigApi.getLong("comparison_quiz_skip_cost") } returns skipCost.toLong()
        coEvery { userRepository.getLocalUserDiamonds() } returns userDiamonds
        coEvery { userRepository.addLocalUserDiamonds(-skipCost.toInt()) } just runs

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

        comparisonQuizCoreImpl.skip()

        // Verify that we called this twice, one for checking if can skip and one for actually skipping
        verify(exactly = 2) { remoteConfigApi.getLong("comparison_quiz_skip_cost") }

        coVerify(exactly = 1) { userRepository.getLocalUserDiamonds() }
        coVerify(exactly = 1) { userRepository.addLocalUserDiamonds(-skipCost.toInt()) }

        val newQuizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val newCurrentQuestion = newQuizData.currentQuestion

        assertThat(newQuizData.isGameOver).isFalse()
        assertThat(newCurrentQuestion).isNotNull()
        require(newCurrentQuestion != null)

        assertThat(newQuizData.questions).isEmpty()

        assertThat(newCurrentQuestion.questions.first).isEqualTo(expectedQuestions[1])
        assertThat(newCurrentQuestion.questions.second).isEqualTo(expectedQuestions[2])
    }

    @Test
    fun `skip should throw exception when user doesn't have enough diamonds`() = runTest {
        val skipCost = 10u
        val userDiamonds = 5

        every { remoteConfigApi.getLong("comparison_quiz_skip_cost") } returns skipCost.toLong()
        coEvery { userRepository.getLocalUserDiamonds() } returns userDiamonds

        // Assert exception is thrown
        assertThrows<RuntimeException> {
            comparisonQuizCoreImpl.skip()
        }

        // Verify interactions

        // Verify that we called this once, one for checking if can skip
        // We don't call it again because we don't have enough diamonds
        verify(exactly = 1) { remoteConfigApi.getLong("comparison_quiz_skip_cost") }

        coVerify(exactly = 1) { userRepository.getLocalUserDiamonds() }
        coVerify(exactly = 0) { userRepository.addLocalUserDiamonds(any()) }
        confirmVerified(remoteConfigApi, userRepository)
    }
}