package com.infinitepower.newquiz.comparison_quiz.core

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
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
import com.infinitepower.newquiz.core.user_services.LocalUserServiceImpl
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.core.user_services.data.xp.ComparisonQuizXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.data.xp.MultiChoiceQuizXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.data.xp.WordleXpGeneratorImpl
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCurrentQuestion
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizHelperValueState
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.toUiText
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
@OptIn(ExperimentalCoroutinesApi::class)
internal class ComparisonQuizCoreImplTest {
    private lateinit var comparisonQuizCoreImpl: ComparisonQuizCoreImpl

    private val comparisonQuizRepository = mockk<ComparisonQuizRepository>()
    private val remoteConfig = mockk<RemoteConfig>()

    private lateinit var userService: UserService

    @TempDir
    lateinit var tmpDir: File

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var gameResultDao: GameResultDao

    @BeforeEach
    fun setup() {
        val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = testScope,
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

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @Test
    fun `initializeGame should emit correct data`() = testScope.runTest {
        val initialData = getInitializationData()
        val uriMock = getUriMock()

        val firstItemHelperValueState = ComparisonQuizHelperValueState.HIDDEN
        every {
            remoteConfig.getString("comparison_quiz_first_item_helper_value")
        } returns firstItemHelperValueState.name

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
        assertThat(quizData.firstItemHelperValueState).isEqualTo(firstItemHelperValueState)

        val expectedCurrentQuestion = ComparisonQuizCurrentQuestion(
            questions = expectedQuestions[0] to expectedQuestions[1]
        )

        // verify current question
        assertThat(quizData.currentQuestion).isNotNull()
        assertThat(quizData.currentQuestion).isEqualTo(expectedCurrentQuestion)
    }

    @Test
    fun `initializeGame should end game when error in data request`() = testScope.runTest {
        val initialData = getInitializationData()

        every {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns flowOf(Resource.Error("error"))

        comparisonQuizCoreImpl.initializeGame(initialData)

        val quizData = comparisonQuizCoreImpl.quizDataFlow.first()
        assertThat(quizData.isGameOver).isTrue()
        assertThat(quizData.currentQuestion).isNull()
    }

    @Test
    fun `onAnswerClicked should check the correct answer and move to the next question`() = testScope.runTest {
        val initialData = getInitializationData(
            comparisonMode = ComparisonMode.LESSER
        )
        val uriMock = getUriMock()

        val firstItemHelperValueState = ComparisonQuizHelperValueState.HIDDEN
        every {
            remoteConfig.getString("comparison_quiz_first_item_helper_value")
        } returns firstItemHelperValueState.name

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
    fun `onAnswerClicked should check the wrong answer and move to the next question`() = testScope.runTest {
        val initialData = getInitializationData(
            comparisonMode = ComparisonMode.LESSER
        )
        val uriMock = getUriMock()

        val firstItemHelperValueState = ComparisonQuizHelperValueState.HIDDEN
        every {
            remoteConfig.getString("comparison_quiz_first_item_helper_value")
        } returns firstItemHelperValueState.name

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
    fun `onAnswerClicked should end the game when no more questions is remaining`() = testScope.runTest {
        val initialData = getInitializationData()
        val uriMock = getUriMock()

        val firstItemHelperValueState = ComparisonQuizHelperValueState.HIDDEN
        every {
            remoteConfig.getString("comparison_quiz_first_item_helper_value")
        } returns firstItemHelperValueState.name

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

        every {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns flowOf(Resource.Success(expectedQuestions))

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
    fun `endGame() should end the game`() = testScope.runTest {
        val initialData = getInitializationData()
        val uriMock = getUriMock()

        val firstItemHelperValueState = ComparisonQuizHelperValueState.HIDDEN
        every {
            remoteConfig.getString("comparison_quiz_first_item_helper_value")
        } returns firstItemHelperValueState.name

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

        every {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns flowOf(Resource.Success(expectedQuestions))

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
    fun `canSkip should return true when user has enough diamonds`() = testScope.runTest {
        val skipCost = 10
        val userDiamonds = 15

        every { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) } returns skipCost
        every { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns userDiamonds
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(userDiamonds).key, userDiamonds)

        val result = comparisonQuizCoreImpl.canSkip()

        // Verify interactions
        verify { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) }
        verify { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) }
        confirmVerified(remoteConfig)

        // Verify result
        assertThat(result).isTrue()
    }

    @Test
    fun `canSkip should return false when user doesn't have enough diamonds`() = testScope.runTest {
        val skipCost = 10
        val userDiamonds = 5

        every { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) } returns skipCost
        every { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns userDiamonds
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(userDiamonds).key, userDiamonds)

        val result = comparisonQuizCoreImpl.canSkip()

        // Verify interactions
        verify { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) }
        verify { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) }

        // Verify result
        assertThat(result).isFalse()

        confirmVerified(remoteConfig)
    }

    @Test
    fun `skip() should deduct diamonds and update quiz data`() = testScope.runTest {
        val skipCost = 1
        val userDiamonds = 10

        val initialData = getInitializationData()

        val firstItemHelperValueState = ComparisonQuizHelperValueState.HIDDEN
        every {
            remoteConfig.getString("comparison_quiz_first_item_helper_value")
        } returns firstItemHelperValueState.name

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

        every {
            comparisonQuizRepository.getQuestions(category = initialData.category)
        } returns flowOf(Resource.Success(expectedQuestions))

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

        // Verify that we called this twice, one for checking if can skip and one for actually skipping
        verify(exactly = 2) { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) }

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

        verify { remoteConfig.getString("comparison_quiz_first_item_helper_value") }
        verify { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) }

        confirmVerified(remoteConfig)
    }

    @Test
    fun `skip should throw exception when user doesn't have enough diamonds`() = testScope.runTest {
        val skipCost = 10
        val userDiamonds = 5

        every { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) } returns skipCost
        every { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns userDiamonds
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(userDiamonds).key, userDiamonds)

        // Assert exception is thrown
        assertThrows<RuntimeException> {
            comparisonQuizCoreImpl.skip()
        }

        // Verify interactions

        // Verify that we called this once, one for checking if can skip
        // We don't call it again because we don't have enough diamonds
        verify(exactly = 1) { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST) }
        verify(exactly = 1) { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) }
        confirmVerified(remoteConfig)
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
            formatType = ComparisonQuizFormatType.Number,
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