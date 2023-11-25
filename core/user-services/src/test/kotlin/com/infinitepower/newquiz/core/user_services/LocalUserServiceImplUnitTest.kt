package com.infinitepower.newquiz.core.user_services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.datastore.common.LocalUserCommon
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.PreferencesDatastoreManager
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.testing.domain.FakeGameResultDao
import com.infinitepower.newquiz.core.user_services.data.xp.MultiChoiceQuizXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.data.xp.WordleXpGeneratorImpl
import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.WordleXpGenerator
import com.infinitepower.newquiz.core.user_services.model.User
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test

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

    private val multiChoiceQuizXpGenerator: MultiChoiceQuizXpGenerator = MultiChoiceQuizXpGeneratorImpl()
    private val wordleXpGenerator: WordleXpGenerator = WordleXpGeneratorImpl()

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

        localUserServiceImpl = LocalUserServiceImpl(
            dataStoreManager = dataStoreManager,
            remoteConfig = remoteConfig,
            gameResultDao = gameResultDao,
            multiChoiceXpGenerator = multiChoiceQuizXpGenerator,
            wordleXpGenerator = wordleXpGenerator
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
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key, INITIAL_DIAMONDS)

        val result = localUserServiceImpl.getUserDiamonds()

        assertThat(result).isEqualTo(INITIAL_DIAMONDS.toUInt())
    }

    @Test
    fun `addRemoveDiamonds() should add diamonds to the user`() = testScope.runTest {
        coEvery { remoteConfig.get(RemoteConfigValue.USER_INITIAL_DIAMONDS) } returns INITIAL_DIAMONDS
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key, INITIAL_DIAMONDS)

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
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key, INITIAL_DIAMONDS)

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
        dataStoreManager.editPreference(LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key, INITIAL_DIAMONDS)

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
}