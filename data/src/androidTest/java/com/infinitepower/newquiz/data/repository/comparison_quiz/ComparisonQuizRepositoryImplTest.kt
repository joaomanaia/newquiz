package com.infinitepower.newquiz.data.repository.comparison_quiz

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.database.model.user.ComparisonQuizGameResultEntity
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes

/**
 * Tests for [ComparisonQuizRepositoryImpl]
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class ComparisonQuizRepositoryImplTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var remoteConfig: RemoteConfig

    @Inject lateinit var gameResultDao: GameResultDao

    @Inject lateinit var comparisonQuizApi: ComparisonQuizApi

    private lateinit var repository: ComparisonQuizRepositoryImpl

    @BeforeTest
    fun setUp() {
        hiltRule.inject()

        repository = ComparisonQuizRepositoryImpl(
            remoteConfig = remoteConfig,
            gameResultDao = gameResultDao,
            comparisonQuizApi = comparisonQuizApi,
        )
    }

    @Test
    fun getHighestPosition_returnsHighestPosition() = runTest {
        // The highest position is not stored in the database
        // So it should return 0
        val noHighestPosition = repository.getHighestPosition(categoryId = "category")
        assertThat(noHighestPosition).isEqualTo(0)

        val now = Clock.System.now()

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

        val highestPosition = repository.getHighestPosition(categoryId = "category")

        assertThat(highestPosition).isEqualTo(5)
    }
}