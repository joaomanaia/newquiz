package com.infinitepower.newquiz.core.user_services

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.AppDatabase
import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.datastore.common.LocalUserCommon
import com.infinitepower.newquiz.core.datastore.di.LocalUserDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.user_services.domain.xp.ComparisonQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.MultiChoiceQuizXpGenerator
import com.infinitepower.newquiz.core.user_services.domain.xp.WordleXpGenerator
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.getBasicMultiChoiceQuestion
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [LocalUserServiceImpl]
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class LocalUserServiceImplTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    private lateinit var localUserServiceImpl: LocalUserServiceImpl

    private val remoteConfig: RemoteConfig = mockk()

    @Inject
    @LocalUserDataStoreManager
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var gameResultDao: GameResultDao

    @Inject
    lateinit var multiChoiceQuizXpGenerator: MultiChoiceQuizXpGenerator

    @Inject
    lateinit var wordleXpGenerator: WordleXpGenerator

    @Inject
    lateinit var comparisonQuizXpGenerator: ComparisonQuizXpGenerator

    companion object {
        private const val INITIAL_DIAMONDS = 10
        private const val NEW_LEVEL_DIAMONDS = 10
    }

    @BeforeTest
    fun setUp() {
        hiltRule.inject()

        runTest {
            dataStoreManager.clearPreferences()

            dataStoreManager.editPreference(LocalUserCommon.UserUid.key, "uid")
            dataStoreManager.editPreference(LocalUserCommon.UserTotalXp.key, 0)
            dataStoreManager.editPreference(
                LocalUserCommon.UserDiamonds(INITIAL_DIAMONDS).key,
                INITIAL_DIAMONDS
            )
        }

        localUserServiceImpl = LocalUserServiceImpl(
            dataStoreManager = dataStoreManager,
            remoteConfig = remoteConfig,
            gameResultDao = gameResultDao,
            multiChoiceXpGenerator = multiChoiceQuizXpGenerator,
            wordleXpGenerator = wordleXpGenerator,
            comparisonQuizXpGenerator = comparisonQuizXpGenerator
        )
    }

    @AfterTest
    fun tearDown() {
        appDatabase.close()

        runTest {
            dataStoreManager.clearPreferences()
        }
    }

    @Test
    fun test_saveMultiChoiceGame_whenGenerateXpIsEnabled() = runTest {
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
            assertThat(updatedUser.diamonds).isEqualTo(updatedUser.diamonds + NEW_LEVEL_DIAMONDS.toUInt())
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
    fun test_saveMultiChoiceGame_whenGenerateXpIsDisabled() = runTest {
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
}