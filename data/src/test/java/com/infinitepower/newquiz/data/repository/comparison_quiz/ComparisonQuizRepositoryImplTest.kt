package com.infinitepower.newquiz.data.repository.comparison_quiz

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.model.NumberFormatType
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [ComparisonQuizRepositoryImpl]
 */
internal class ComparisonQuizRepositoryImplTest {
    private lateinit var repository: ComparisonQuizRepositoryImpl

    private val remoteConfig: RemoteConfig = mockk()
    private val gameResultDao: GameResultDao = mockk()
    private val comparisonQuizApi: ComparisonQuizApi = mockk()

    @BeforeTest
    fun setUp() {
        repository = ComparisonQuizRepositoryImpl(
            remoteConfig = remoteConfig,
            gameResultDao = gameResultDao,
            comparisonQuizApi = comparisonQuizApi
        )
    }

    @Test
    fun `getCategories() should return list of categories`() {
        val categories = List(10) { id ->
            ComparisonQuizCategory(
                id = id.toString(),
                name = UiText.DynamicString("name $id"),
                image = "image$id",
                description = "description $id",
                formatType = NumberFormatType.DEFAULT,
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "greater $id",
                    less = "less $id"
                )
            )
        }

        val categoriesEntity = categories.map { it.toEntity() }

        every {
            remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_CATEGORIES)
        } returns Json.encodeToString(categoriesEntity)

        val result = repository.getCategories()

        assertThat(result).containsExactlyElementsIn(categories)

        verify(exactly = 1) { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_CATEGORIES) }

        // Check if the second call to getCategories() returns the same list of categories cached
        // and does not call remoteConfig.get() again

        val result2 = repository.getCategories()

        assertThat(result2).containsExactlyElementsIn(categories)

        // Not called again
        verify(exactly = 1) { remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_CATEGORIES) }

        confirmVerified(remoteConfig)
    }

    @Test
    fun `getHighestPosition() should return highest position`() = runTest {
        val highestPosition = 10

        coEvery {
            gameResultDao.getComparisonQuizHighestPosition("1")
        } returns highestPosition

        val result = repository.getHighestPosition("1")

        assertThat(result).isEqualTo(highestPosition)

        coVerify(exactly = 1) { gameResultDao.getComparisonQuizHighestPosition("1") }

        confirmVerified(gameResultDao)
    }

    @Test
    fun `getHighestPositionFlow() should return highest position`() = runTest {
        val highestPosition = 10

        every {
            gameResultDao.getComparisonQuizHighestPositionFlow("1")
        } returns flowOf(highestPosition)

        val result = repository.getHighestPositionFlow("1")

        result.test {
            assertThat(awaitItem()).isEqualTo(highestPosition)
            awaitComplete()
        }

        verify(exactly = 1) { gameResultDao.getComparisonQuizHighestPositionFlow("1") }

        confirmVerified(gameResultDao)
    }

    @Test
    fun `getQuestions() should return list of questions`() = runTest {
        val questionsToGenerate = 10
        val questionsEntity = List(questionsToGenerate) { id ->
            ComparisonQuizItemEntity(
                title = "title $id",
                value = id.toDouble(),
                imgUrl = "",
            )
        }

        val category = ComparisonQuizCategory(
            id = "1",
            name = UiText.DynamicString("name"),
            image = "image",
            description = "description",
            formatType = NumberFormatType.DEFAULT,
            questionDescription = ComparisonQuizCategory.QuestionDescription(
                greater = "greater",
                less = "less"
            )
        )

        coEvery {
            comparisonQuizApi.generateQuestions(
                category = category,
                size = questionsToGenerate,
            )
        } returns questionsEntity

        val result = repository.getQuestions(
            category = category,
            size = questionsToGenerate,
        )

        result.forEachIndexed { index, question ->
            assertThat(question.title).isNotEmpty()
            assertThat(question.value).isEqualTo(index.toDouble())
            assertThat(question.imgUri).isNotNull()
        }

        coVerify(exactly = 1) {
            comparisonQuizApi.generateQuestions(
                category = category,
                size = questionsToGenerate,
            )
        }

        confirmVerified(comparisonQuizApi)
    }
}
