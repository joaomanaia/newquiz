package com.infinitepower.newquiz.core.user_services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.database.model.user.ComparisonQuizGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import com.infinitepower.newquiz.core.database.model.user.WordleGameResultEntity
import com.infinitepower.newquiz.core.datastore.common.LocalUserCommon
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.PreferencesDatastoreManager
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.testing.domain.FakeGameResultDao
import com.infinitepower.newquiz.core.user_services.data.xp.ComparisonQuizXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.data.xp.MultiChoiceQuizXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.data.xp.WordleXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.domain.xp.ComparisonQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.WordleXpGenerator
import com.infinitepower.newquiz.core.user_services.model.User
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes
import kotlin.time.measureTimedValue

/**
 * Tests for [LocalUserServiceImpl]
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class LocalUserServiceImplUnitTest {
    @TempDir
    lateinit var tmpDir: File

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher + Job())

    private lateinit var dataStoreManager: DataStoreManager

    private val remoteConfig: RemoteConfig = mockk()
    private lateinit var gameResultDao: GameResultDao

    private val multiChoiceQuizXpGenerator: MultiChoiceQuizXpGenerator =
        MultiChoiceQuizXpGeneratorImpl(remoteConfig)
    private val wordleXpGenerator: WordleXpGenerator = WordleXpGeneratorImpl(remoteConfig)
    private val comparisonQuizXpGenerator: ComparisonQuizXpGenerator =
        ComparisonQuizXpGeneratorImpl(remoteConfig)

    private lateinit var localUserServiceImpl: LocalUserServiceImpl

    companion object {
        private const val INITIAL_DIAMONDS = 10
        private const val NEW_LEVEL_DIAMONDS = 10
    }

    @BeforeTest
    fun setUp() {
        val testDataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(tmpDir, "user.preferences_pb") }
        )

        dataStoreManager = PreferencesDatastoreManager(testDataStore)

        gameResultDao = FakeGameResultDao()

        every { remoteConfig.get(RemoteConfigValue.WORDLE_DEFAULT_XP_REWARD) } returns 10
        every { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_DEFAULT_XP_REWARD) } returns 10
        every { remoteConfig.get(RemoteConfigValue.MULTICHOICE_QUIZ_DEFAULT_XP_REWARD) } returns """
            {
                "easy": 10,
                "medium": 20,
                "hard": 30
            }
        """.trimIndent()

        localUserServiceImpl = LocalUserServiceImpl(
            dataStoreManager = dataStoreManager,
            remoteConfig = remoteConfig,
            gameResultDao = gameResultDao,
            multiChoiceXpGenerator = multiChoiceQuizXpGenerator,
            wordleXpGenerator = wordleXpGenerator,
            comparisonQuizXpGenerator = comparisonQuizXpGenerator
        )
    }

    @Test
    fun `userAvailable() should return true when uid is not blank`() = testScope.runTest {
        dataStoreManager.editPreference(LocalUserCommon.UserUid.key, "uid")

        val result = localUserServiceImpl.userAvailable()
        assertThat(result).isTrue()
    }

    @Test
    fun `getUserDiamonds() should return the user diamonds`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        dataStoreManager.editPreference(
            LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key,
            INITIAL_DIAMONDS
        )

        val result = localUserServiceImpl.getUserDiamonds()

        assertThat(result).isEqualTo(INITIAL_DIAMONDS.toUInt())
    }

    @Test
    fun `getUserDiamonds() should return the initial diamonds when the user diamonds are not set`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS

        val result = localUserServiceImpl.getUserDiamonds()

        assertThat(result).isEqualTo(INITIAL_DIAMONDS.toUInt())
    }

    @Test
    fun `getUserDiamondsFlow() should return the user diamonds`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        dataStoreManager.editPreference(
            LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key,
            INITIAL_DIAMONDS
        )

        localUserServiceImpl.getUserDiamondsFlow().test {
            assertThat(awaitItem()).isEqualTo(INITIAL_DIAMONDS.toUInt())
        }
    }

    @Test
    fun `addRemoveDiamonds() should add diamonds to the user`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        dataStoreManager.editPreference(
            LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key,
            INITIAL_DIAMONDS
        )

        val diamondsToAdd = 5

        localUserServiceImpl.addRemoveDiamonds(diamondsToAdd)

        // Check that the diamonds were added
        val expectedDiamonds = INITIAL_DIAMONDS + diamondsToAdd
        val result = dataStoreManager.getPreference(LocalUserCommon.UserDiamonds(expectedDiamonds))
        assertThat(result).isEqualTo(expectedDiamonds)
    }

    @Test
    fun `updateNewLevelDiamonds() should update the user diamonds`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        dataStoreManager.editPreference(
            LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key,
            INITIAL_DIAMONDS
        )

        coEvery { remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS) } returns NEW_LEVEL_DIAMONDS

        localUserServiceImpl.updateNewLevelDiamonds()

        // Check that the diamonds were added
        val expectedDiamonds = INITIAL_DIAMONDS + NEW_LEVEL_DIAMONDS
        val result = dataStoreManager.getPreference(LocalUserCommon.UserDiamonds(expectedDiamonds))
        assertThat(result).isEqualTo(expectedDiamonds)
    }

    @Test
    fun `getUser() should return null when uid is blank`() = testScope.runTest {
        dataStoreManager.editPreference(LocalUserCommon.UserUid.key, "")

        val result = localUserServiceImpl.getUser()
        assertThat(result).isNull()
    }

    @Test
    fun `getUser() should return User when uid is not blank`() = testScope.runTest {
        dataStoreManager.editPreference(LocalUserCommon.UserUid.key, "uid")
        dataStoreManager.editPreference(LocalUserCommon.UserTotalXp.key, 0)

        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        dataStoreManager.editPreference(
            LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key,
            INITIAL_DIAMONDS
        )

        val result = localUserServiceImpl.getUser()

        assertThat(result).isNotNull()

        val expectedUser = User(
            uid = "uid",
            totalXp = 0u,
            diamonds = INITIAL_DIAMONDS.toUInt()
        )
        assertThat(result).isEqualTo(expectedUser)
    }

    @CsvSource(
        "0",
        "100",
        "399",
        "1000"
    )
    @ParameterizedTest(name = "test getNextLevelXp() when totalXp is {0}")
    fun `test saveMultiChoiceGame when generate xp is enabled`(
        initialXp: Long
    ) = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        coEvery { remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS) } returns NEW_LEVEL_DIAMONDS

        dataStoreManager.editPreference(LocalUserCommon.UserTotalXp.key, initialXp)

        // Average answer time is 8 seconds
        val questionSteps = listOf(
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = true,
                questionTime = 10
            ),
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = false,
                questionTime = 8
            ),
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = true,
                questionTime = 6
            ),
        )

        val initialUser = localUserServiceImpl.getUser()
        require(initialUser != null)

        localUserServiceImpl.saveMultiChoiceGame(
            questionSteps = questionSteps,
            generateXp = true
        )

        // Check that the user's total xp has been updated
        val updatedUser = localUserServiceImpl.getUser()
        require(updatedUser != null)

        val expectedXp = multiChoiceQuizXpGenerator.generateXp(questionSteps)

        // Check if the new xp is in the range of the generated xp
        val newXp = updatedUser.totalXp - initialUser.totalXp
        assertThat(newXp.toInt()).isEqualTo(expectedXp.toInt())

        // If the user is in new level, check if the diamonds have been updated
        if (initialUser.isNewLevel(newXp)) {
            assertThat(updatedUser.diamonds).isEqualTo(initialUser.diamonds + NEW_LEVEL_DIAMONDS.toUInt())
        } else {
            assertThat(updatedUser.diamonds).isEqualTo(initialUser.diamonds)
        }

        // Check if the game result has been saved
        val gameResults = gameResultDao.getMultiChoiceResults()

        assertThat(gameResults).hasSize(1)

        // Because there is only one game result, we can assume that the first one is the one we want
        gameResults.first().apply {
            assertThat(correctAnswers).isEqualTo(questionSteps.count { it.correct })
            assertThat(questionCount).isEqualTo(questionSteps.count())
            assertThat(averageAnswerTime).isEqualTo(
                questionSteps.map { it.questionTime }.average()
            )
            assertThat(earnedXp).isEqualTo(newXp.toInt())
        }
    }

    @Test
    fun `test saveMultiChoiceGame when generate xp is disabled`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        coEvery { remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS) } returns NEW_LEVEL_DIAMONDS

        // Average answer time is 8 seconds
        val questionSteps = listOf(
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = true,
                questionTime = 10
            ),
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = false,
                questionTime = 8
            ),
            MultiChoiceQuestionStep.Completed(
                question = getBasicMultiChoiceQuestion(),
                correct = true,
                questionTime = 6
            ),
        )

        val initialUser = localUserServiceImpl.getUser()
        require(initialUser != null)

        localUserServiceImpl.saveMultiChoiceGame(
            questionSteps = questionSteps,
            generateXp = false
        )

        val updatedUser = localUserServiceImpl.getUser()
        require(updatedUser != null)

        // because the xp generation is disabled, the new xp should be 0
        val newXp = updatedUser.totalXp - initialUser.totalXp
        assertThat(newXp).isEqualTo(0uL)

        // Check if the game result has been saved
        val gameResults = gameResultDao.getMultiChoiceResults()

        assertThat(gameResults).hasSize(1)
    }

    @CsvSource(
        "0",
        "100",
        "399",
        "1000"
    )
    @ParameterizedTest(name = "test saveWordleGame when generate xp is enabled and totalXp is {0}")
    fun `test saveWordleGame when generate xp is enabled`(
        initialXp: Long
    ) = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        coEvery { remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS) } returns NEW_LEVEL_DIAMONDS

        dataStoreManager.editPreference(LocalUserCommon.UserTotalXp.key, initialXp)

        val initialUser = localUserServiceImpl.getUser()
        require(initialUser != null)

        localUserServiceImpl.saveWordleGame(
            wordLength = 5u,
            rowsUsed = 3u,
            maxRows = Int.MAX_VALUE,
            categoryId = "category",
            generateXp = true
        )

        // Check that the user's total xp has been updated
        val updatedUser = localUserServiceImpl.getUser()
        require(updatedUser != null)

        val expectedXp = wordleXpGenerator.generateXp(rowsUsed = 3u)

        // Check if the new xp is equal to the generated xp
        val newXp = updatedUser.totalXp - initialUser.totalXp
        assertThat(newXp.toInt()).isEqualTo(expectedXp.toInt())

        // If the user is in new level, check if the diamonds have been updated
        if (initialUser.isNewLevel(newXp)) {
            assertThat(updatedUser.diamonds).isEqualTo(initialUser.diamonds + NEW_LEVEL_DIAMONDS.toUInt())
        } else {
            assertThat(updatedUser.diamonds).isEqualTo(initialUser.diamonds)
        }

        // Check if the game result has been saved
        val gameResults = gameResultDao.getWordleResults()
        assertThat(gameResults).hasSize(1)

        // Because there is only one game result, we can assume that the first one is the one we want
        gameResults.first().apply {
            assertThat(wordLength).isEqualTo(5)
            assertThat(rowsUsed).isEqualTo(3)
            assertThat(maxRows).isEqualTo(Int.MAX_VALUE)
            assertThat(earnedXp).isEqualTo(newXp.toInt())
        }
    }

    @Test
    fun `test saveWordleGame when generate xp is disabled`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        coEvery { remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS) } returns NEW_LEVEL_DIAMONDS

        val initialUser = localUserServiceImpl.getUser()
        require(initialUser != null)

        localUserServiceImpl.saveWordleGame(
            wordLength = 5u,
            rowsUsed = 3u,
            maxRows = Int.MAX_VALUE,
            categoryId = "category",
            generateXp = false
        )

        // Check that the user's total xp has been updated
        val updatedUser = localUserServiceImpl.getUser()
        require(updatedUser != null)

        // because the xp generation is disabled, the new xp should be 0
        val newXp = updatedUser.totalXp - initialUser.totalXp
        assertThat(newXp).isEqualTo(0uL)

        // Check if the game result has been saved
        val gameResults = gameResultDao.getWordleResults()
        assertThat(gameResults).hasSize(1)
    }

    @CsvSource(
        "0",
        "100",
        "399",
        "1000"
    )
    @ParameterizedTest(name = "test saveComparisonQuizGame when generate xp is enabled and totalXp is {0}")
    fun `test saveComparisonQuizGame when generate xp is enabled`(
        initialXp: Long
    ) = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        coEvery { remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS) } returns NEW_LEVEL_DIAMONDS

        dataStoreManager.editPreference(LocalUserCommon.UserTotalXp.key, initialXp)

        val initialUser = localUserServiceImpl.getUser()
        require(initialUser != null)

        val endPosition = 5u

        localUserServiceImpl.saveComparisonQuizGame(
            categoryId = "category",
            comparisonMode = ComparisonMode.GREATER.name,
            endPosition = endPosition,
            skippedAnswers = 1u,
            generateXp = true
        )

        // Check that the user's total xp has been updated
        val updatedUser = localUserServiceImpl.getUser()
        require(updatedUser != null)

        val expectedXp = comparisonQuizXpGenerator.generateXp(
            endPosition = endPosition,
            skippedAnswers = 1u,
        )

        // Check if the new xp is equal to the generated xp
        val newXp = updatedUser.totalXp - initialUser.totalXp
        assertThat(newXp.toInt()).isEqualTo(expectedXp.toInt())

        // If the user is in new level, check if the diamonds have been updated
        if (initialUser.isNewLevel(newXp)) {
            assertThat(updatedUser.diamonds).isEqualTo(initialUser.diamonds + NEW_LEVEL_DIAMONDS.toUInt())
        } else {
            assertThat(updatedUser.diamonds).isEqualTo(initialUser.diamonds)
        }

        // Check if the game result has been saved
        val gameResults = gameResultDao.getComparisonQuizResults()
        assertThat(gameResults).hasSize(1)

        // Because there is only one game result, we can assume that the first one is the one we want
        gameResults.first().apply {
            assertThat(comparisonMode).isEqualTo(ComparisonMode.GREATER.name)
            assertThat(this.endPosition).isEqualTo(endPosition.toInt())
            assertThat(earnedXp).isEqualTo(newXp.toInt())
        }
    }

    @Test
    fun `test saveComparisonQuizGame when generate xp is disabled`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        coEvery { remoteConfig.get(RemoteConfigValue.NEW_LEVEL_DIAMONDS) } returns NEW_LEVEL_DIAMONDS

        val initialUser = localUserServiceImpl.getUser()
        require(initialUser != null)

        val endPosition = 5u

        localUserServiceImpl.saveComparisonQuizGame(
            categoryId = "category",
            comparisonMode = ComparisonMode.GREATER.name,
            endPosition = endPosition,
            skippedAnswers = 0u,
            generateXp = false
        )

        // Check that the user's total xp has been updated
        val updatedUser = localUserServiceImpl.getUser()
        require(updatedUser != null)

        // because the xp generation is disabled, the new xp should be 0
        val newXp = updatedUser.totalXp - initialUser.totalXp
        assertThat(newXp).isEqualTo(0uL)

        // Check if the game result has been saved
        val gameResults = gameResultDao.getComparisonQuizResults()
        assertThat(gameResults).hasSize(1)
    }

    @Test
    fun `test getXpEarnedByRange() and getXpEarnedInLastDays()`() = testScope.runTest {
        val now = Clock.System.now()
        val tz = TimeZone.currentSystemDefault()

        val startInstant = now - 7.days // 7 days ago
        val startDate = startInstant.toLocalDateTime(tz).date
        val endDate = now.toLocalDateTime(tz).date

        // Insert multi choice results
        gameResultDao.insertMultiChoiceResult(
            MultiChoiceGameResultEntity(
                correctAnswers = 0,
                skippedQuestions = 0,
                questionCount = 0,
                averageAnswerTime = 0.0,
                earnedXp = 5,
                playedAt = (now - 1.minutes).toEpochMilliseconds() // today
            ),
            MultiChoiceGameResultEntity(
                correctAnswers = 0,
                skippedQuestions = 0,
                questionCount = 0,
                averageAnswerTime = 0.0,
                earnedXp = 10,
                playedAt = (now - 2.minutes).toEpochMilliseconds() // today
            ),
            MultiChoiceGameResultEntity(
                correctAnswers = 0,
                skippedQuestions = 0,
                questionCount = 0,
                averageAnswerTime = 0.0,
                earnedXp = 20,
                playedAt = (now - 1.days).toEpochMilliseconds() // yesterday
            ),
            // Insert a result that is not in the current week
            MultiChoiceGameResultEntity(
                correctAnswers = 0,
                skippedQuestions = 0,
                questionCount = 0,
                averageAnswerTime = 0.0,
                earnedXp = 20,
                playedAt = (now - 10.days).toEpochMilliseconds() // 10 days ago
            ),
        )

        // Insert wordle results
        gameResultDao.insertWordleResult(
            WordleGameResultEntity(
                earnedXp = 10,
                playedAt = (now - 2.days).toEpochMilliseconds(), // before yesterday
                wordLength = 5,
                rowsUsed = 3,
                maxRows = Int.MAX_VALUE,
                categoryId = "category"
            ),
        )

        // Insert comparison quiz results
        gameResultDao.insertComparisonQuizResult(
            ComparisonQuizGameResultEntity(
                earnedXp = 10,
                playedAt = (now - 4.minutes).toEpochMilliseconds(), // today
                comparisonMode = ComparisonMode.GREATER.name,
                endPosition = 5,
                categoryId = "category",
                skippedAnswers = 0
            )
        )

        // Results:
        // 3 results today
        // 1 result yesterday
        // 1 result before yesterday
        // 1 result 10 days ago (not in the current week, should not be returned)

        // XP for days:
        // today: 25
        // yesterday: 20
        // before yesterday: 10

        // Check if the results are returned
        val resultTimed = measureTimedValue {
            localUserServiceImpl.getXpEarnedBy(
                start = startInstant,
                end = now
            )
        }

        val resultLast7Days = localUserServiceImpl.getXpEarnedBy(TimeRange.ThisWeek)

        println("Time taken: ${resultTimed.duration}")

        val result = resultTimed.value

        assertThat(result).isEqualTo(resultLast7Days)

        assertThat(result).hasSize(3)
        // Check if the results are sorted by playedAt
        assertThat(result.map { it.key }).isInOrder()

        result.forEach { (date, _) ->
            // Check if the result from the other week is not returned
            assertThat(date).isAtLeast(startDate.toEpochDays())
            assertThat(date).isAtMost(endDate.toEpochDays())
        }

        // Check if the xp is correct
        println(result)
        assertThat(result[now.toLocalDateTime(tz).date.toEpochDays()]).isEqualTo(25) // today
        assertThat(result[(now - 1.days).toLocalDateTime(tz).date.toEpochDays()]).isEqualTo(20) // yesterday
        assertThat(result[(now - 2.days).toLocalDateTime(tz).date.toEpochDays()]).isEqualTo(10) // before yesterday

        // Test getXpEarnedByRangeFlow() because it is same as getXpEarnedByRange()
        localUserServiceImpl.getXpEarnedByFlow(TimeRange.ThisWeek).test {
            assertThat(awaitItem()).isEqualTo(result)
        }
    }
}