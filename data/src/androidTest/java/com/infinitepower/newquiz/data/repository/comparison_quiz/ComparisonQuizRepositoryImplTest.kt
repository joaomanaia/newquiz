package com.infinitepower.newquiz.data.repository.comparison_quiz

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.dao.ComparisonQuizDao
import com.infinitepower.newquiz.core.database.model.ComparisonQuizHighestPosition
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.ktor.client.HttpClient
import kotlin.test.BeforeTest
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.Test

/**
 * Tests for [ComparisonQuizRepositoryImpl]
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class ComparisonQuizRepositoryImplTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var remoteConfig: RemoteConfig

    @Inject lateinit var comparisonQuizDao: ComparisonQuizDao

    private lateinit var repository: ComparisonQuizRepositoryImpl

    @BeforeTest
    fun setUp() {
        hiltRule.inject()

        val engine = MockEngine { request ->
            respond(
                content = ByteReadChannel.Empty,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(engine)

        repository = ComparisonQuizRepositoryImpl(
            client = client,
            remoteConfig = remoteConfig,
            comparisonQuizDao = comparisonQuizDao
        )
    }

    @Test
    fun getHighestPosition_returnsHighestPosition() = runTest {
        // The highest position is not stored in the database
        // So it should return 0
        val noHighestPosition = repository.getHighestPosition(categoryId = "1")

        assertThat(noHighestPosition).isEqualTo(0)

        comparisonQuizDao.upsert(
            ComparisonQuizHighestPosition(
                categoryId = "1",
                highestPosition = 1
            )
        )

        val highestPosition = repository.getHighestPosition(categoryId = "1")

        assertThat(highestPosition).isEqualTo(1)
    }

    @Test
    fun saveHighestPosition_savesHighestPosition() = runTest {
        val categoryId = "1"

        repository.saveHighestPosition(
            categoryId = categoryId,
            position = 1
        )

        val highestPosition = comparisonQuizDao.getHighestPosition(categoryId)
        assertThat(highestPosition?.highestPosition).isEqualTo(1)
    }
}