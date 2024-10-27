package com.infinitepower.newquiz.comparison_quiz.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.analytics.NoOpAnalyticsHelper
import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.datastore.common.LocalUserCommon
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.PreferencesDatastoreManager
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.testing.domain.FakeGameResultDao
import com.infinitepower.newquiz.core.testing.utils.mockAndroidLog
import com.infinitepower.newquiz.core.user_services.InsufficientDiamondsException
import com.infinitepower.newquiz.core.user_services.LocalUserServiceImpl
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.core.user_services.data.xp.ComparisonQuizXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.data.xp.MultiChoiceQuizXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.data.xp.WordleXpGeneratorImpl
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.NumberFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCurrentQuestion
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizHelperValueState
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.toUiText
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.net.URI

/**
 * Unit tests for [ComparisonQuizCoreImpl].
 */
internal class ComparisonQuizCoreImplTest {
    private lateinit var comparisonQuizCoreImpl: ComparisonQuizCoreImpl

    private val comparisonQuizRepository = mockk<ComparisonQuizRepository>()
    private val remoteConfig = mockk<RemoteConfig>()

    private lateinit var userService: UserService

    @TempDir
    lateinit var tmpDir: File

    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var gameResultDao: GameResultDao

    private val firstItemHelperValueState = ComparisonQuizHelperValueState.HIDDEN

    @BeforeEach
    fun setup() {
        mockAndroidLog()
        val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            produceFile = { File(tmpDir, "user.preferences_pb") }
        )

        dataStoreManager = PreferencesDatastoreManager(testDataStore)

        gameResultDao = FakeGameResultDao()

        userService = LocalUserServiceImpl(
            dataStoreManager = dataStoreManager,
            remoteConfig = remoteConfig,
            gameResultDao = gameResultDao,
            multiChoiceXpGenerator = MultiChoiceQuizXpGeneratorImpl(remoteConfig),
            wordleXpGenerator = WordleXpGeneratorImpl(remoteConfig),
            comparisonQuizXpGenerator = ComparisonQuizXpGeneratorImpl(remoteConfig)
        )

        comparisonQuizCoreImpl = ComparisonQuizCoreImpl(
            comparisonQuizRepository = comparisonQuizRepository,
            remoteConfig = remoteConfig,
            analyticsHelper = NoOpAnalyticsHelper,
            userService = userService
        )

        every {
            remoteConfig.getString("comparison_quiz_first_item_helper_value")
        } returns firstItemHelperValueState.name
    }

    @Test
    fun `initializeGame should emit correct data`() = runTest {
        val initialData = getInitializationData()
        val uriMock = getUriMock()

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

        coEvery {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns expectedQuestions

        comparisonQuizCoreImpl.initializeGame(initialData)

        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        assertThat(quizData.comparisonMode).isEqualTo(initialData.comparisonMode)
        assertThat(quizData.questions).contains(expectedQuestions[2])
        assertThat(quizData.firstItemHelperValueState).isEqualTo(firstItemHelperValueState)

        val expectedCurrentQuestion = ComparisonQuizCurrentQuestion(
            questions = expectedQuestions[0] to expectedQuestions[1]
        )

        // verify current question
        assertThat(quizData.currentQuestion).isNotNull()
        assertThat(quizData.currentQuestion).isEqualTo(expectedCurrentQuestion)
    }

    @Test
    fun `initializeGame should end game when error in data request`() = runTest {
        val initialData = getInitializationData()

        coEvery {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns emptyList()

        comparisonQuizCoreImpl.initializeGame(initialData)

        comparisonQuizCoreImpl.quizDataFlow.test {
            val quizData = awaitItem()
            assertThat(quizData.isGameOver).isTrue()
            assertThat(quizData.currentQuestion).isNull()
        }
    }

    @Test
    fun `onAnswerClicked should check the correct answer and move to the next question`() = runTest {
        val initialData = getInitializationData(
            comparisonMode = ComparisonMode.LESSER
        )
        val uriMock = getUriMock()

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

        coEvery {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns expectedQuestions

        comparisonQuizCoreImpl.initializeGame(initialData)

        // verify current question
        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val currentQuestion = quizData.currentQuestion

        assertThat(quizData.comparisonMode).isEqualTo(initialData.comparisonMode)

        // Check if the helper value is correct
        assertThat(quizData.firstItemHelperValueState).isEqualTo(firstItemHelperValueState)

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

        // Check if the helper value is changed
        assertThat(newQuizData.firstItemHelperValueState).isNotEqualTo(firstItemHelperValueState)
        assertThat(newQuizData.firstItemHelperValueState).isEqualTo(ComparisonQuizHelperValueState.NORMAL)
    }

    // Test when the user gets the answer wrong
    @Test
    fun `onAnswerClicked should check the wrong answer and move to the next question`() = runTest {
        val initialData = getInitializationData(
            comparisonMode = ComparisonMode.LESSER
        )
        val uriMock = getUriMock()

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

        coEvery {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns expectedQuestions

        // Loads the initial data and starts the game
        // This will also set the current question
        // The answer will be wrong
        comparisonQuizCoreImpl.initializeGame(initialData)

        // Get the current data and question
        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val currentQuestion = quizData.currentQuestion

        // Check if comparison mode is correct
        assertThat(quizData.comparisonMode).isEqualTo(initialData.comparisonMode)

        // Check if the helper value is correct
        assertThat(quizData.firstItemHelperValueState).isEqualTo(firstItemHelperValueState)

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
    fun `onAnswerClicked should end the game when no more questions is remaining`() = runTest {
        val initialData = getInitializationData()
        val uriMock = getUriMock()

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
            )
        )

        coEvery {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns expectedQuestions

        comparisonQuizCoreImpl.initializeGame(initialData)

        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        assertThat(quizData.questions).isEmpty()

        val expectedCurrentQuestion = ComparisonQuizCurrentQuestion(
            questions = expectedQuestions[0] to expectedQuestions[1]
        )

        val currentQuestion = quizData.currentQuestion

        // verify current question
        assertThat(currentQuestion).isNotNull()
        assertThat(currentQuestion).isEqualTo(expectedCurrentQuestion)
        require(currentQuestion != null)

        comparisonQuizCoreImpl.onAnswerClicked(currentQuestion.questions.second)

        val newQuizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val newCurrentQuestion = newQuizData.currentQuestion

        assertThat(newCurrentQuestion).isNull()
        assertThat(newQuizData.isGameOver).isTrue()
    }

    @Test
    fun `endGame() should end the game`() = runTest {
        val initialData = getInitializationData()
        val uriMock = getUriMock()

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
            )
        )

        coEvery {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns expectedQuestions

        comparisonQuizCoreImpl.initializeGame(initialData)

        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        assertThat(quizData.questions).isEmpty()

        val expectedCurrentQuestion = ComparisonQuizCurrentQuestion(
            questions = expectedQuestions[0] to expectedQuestions[1]
        )

        val currentQuestion = quizData.currentQuestion

        // verify current question
        assertThat(currentQuestion).isNotNull()
        assertThat(currentQuestion).isEqualTo(expectedCurrentQuestion)
        require(currentQuestion != null)

        comparisonQuizCoreImpl.endGame()

        val newQuizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val newCurrentQuestion = newQuizData.currentQuestion

        assertThat(newCurrentQuestion).isNull()
        assertThat(newQuizData.isGameOver).isTrue()
    }

    @Test
    fun `skip() should deduct diamonds and update quiz data`() = runTest {
        val skipCost = 1
        val userDiamonds = 10

        val initialData = getInitializationData()

        every { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) } returns skipCost
        every { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns userDiamonds
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(userDiamonds).key, userDiamonds)

        val uriMock = getUriMock()

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

        coEvery {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns expectedQuestions

        comparisonQuizCoreImpl.initializeGame(initialData)

        // verify current question
        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val currentQuestion = quizData.currentQuestion

        assertThat(quizData.comparisonMode).isEqualTo(initialData.comparisonMode)

        // Check if the helper value is correct
        assertThat(quizData.firstItemHelperValueState).isEqualTo(firstItemHelperValueState)

        assertThat(quizData.isGameOver).isFalse()
        assertThat(quizData.skippedAnswers).isEqualTo(0)
        assertThat(currentQuestion).isNotNull()
        require(currentQuestion != null)

        comparisonQuizCoreImpl.skip()

        verify(exactly = 1) { remoteConfig.getString("comparison_quiz_first_item_helper_value") }
        verify { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) }
        verify(exactly = 1) { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) }
        confirmVerified(remoteConfig)

        val newQuizData = comparisonQuizCoreImpl.quizDataFlow.first()
        val newCurrentQuestion = newQuizData.currentQuestion

        assertThat(newQuizData.isGameOver).isFalse()
        assertThat(newQuizData.skippedAnswers).isEqualTo(1)
        assertThat(newCurrentQuestion).isNotNull()
        require(newCurrentQuestion != null)

        assertThat(newQuizData.questions).isEmpty()

        assertThat(newCurrentQuestion.questions.first).isEqualTo(expectedQuestions[1])
        assertThat(newCurrentQuestion.questions.second).isEqualTo(expectedQuestions[2])

        // Check if the helper value is changed
        assertThat(newQuizData.firstItemHelperValueState).isNotEqualTo(firstItemHelperValueState)
        assertThat(newQuizData.firstItemHelperValueState).isEqualTo(ComparisonQuizHelperValueState.NORMAL)
    }

    @Test
    fun `skip should return when user doesn't have enough diamonds`() = runTest {
        val skipCost = 10
        val userDiamonds = 5

        every { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) } returns skipCost
        every { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns userDiamonds
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(userDiamonds).key, userDiamonds)

        val initialQuizData = comparisonQuizCoreImpl.quizDataFlow.first()

        // Assert exception is thrown
        assertThrows<InsufficientDiamondsException> {
            comparisonQuizCoreImpl.skip()
        }

        // Verify that we called this once, one for checking if can skip
        // We don't call it again because we don't have enough diamonds
        verify(exactly = 1) { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) }
        verify(exactly = 1) { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) }
        confirmVerified(remoteConfig)

        // Check if nothing is changed
        assertThat(userService.getUserDiamonds()).isEqualTo(userDiamonds.toUInt())
        assertThat(comparisonQuizCoreImpl.quizDataFlow.first()).isEqualTo(initialQuizData)
    }

    private fun getInitializationData(
        comparisonMode: ComparisonMode = ComparisonMode.GREATER
    ) = ComparisonQuizCore.InitializationData(
        category = ComparisonQuizCategory(
            id = "id",
            name = "title".toUiText(),
            description = "description",
            image = "imageUrl",
            questionDescription = ComparisonQuizCategory.QuestionDescription(
                greater = "greater",
                less = "less"
            ),
            formatType = NumberFormatType.DEFAULT,
            helperValueSuffix = "helperValueSuffix",
            dataSourceAttribution = ComparisonQuizCategory.DataSourceAttribution(
                text = "text",
                logo = "logo"
            )
        ),
        comparisonMode = comparisonMode
    )

    private fun getUriMock(): URI = URI("test/path")
}
