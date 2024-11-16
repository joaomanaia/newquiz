package com.infinitepower.newquiz.data.repository.comparison_quiz

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.core.testing.utils.mockAndroidLog
import com.infinitepower.newquiz.domain.repository.CountryRepository
import com.infinitepower.newquiz.model.NumberFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import com.infinitepower.newquiz.model.country.Continent
import com.infinitepower.newquiz.model.country.Country
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.toUiText
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import kotlin.test.BeforeTest
import kotlin.test.Test

/**
 * Tests for [ComparisonQuizApiImpl]
 */
internal class ComparisonQuizApiImplTest {
    private lateinit var comparisonQuizApi: ComparisonQuizApiImpl

    private val countryRepository: CountryRepository = mockk()

    private val mockEngine = MockEngine { request ->
        val size = request.url.parameters["size"]?.toIntOrNull() ?: 0

        val randomQuestions = List(size) {
            ComparisonQuizItemEntity(
                title = "Item $it",
                value = it.toDouble(),
                imgUrl = "url$it"
            )
        }

        respond(
            content = ByteReadChannel(Json.encodeToString(randomQuestions)),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    @BeforeTest
    fun setup() {
        mockAndroidLog()
        mockLocalCountries()

        comparisonQuizApi = ComparisonQuizApiImpl(
            client = HttpClient(mockEngine),
            countryRepository = countryRepository
        )
    }

    @Test
    fun `generateQuestions should return questions from remote API when generateQuestionsLocally is false`() =
        runTest {
            val category = getCategory(generateQuestionsLocally = false)
            val size = 10

            val questions = comparisonQuizApi.generateQuestions(
                category = category,
                size = size
            )

            assertThat(mockEngine.requestHistory).hasSize(1)
            assertThat(questions).hasSize(size)

            coVerify(exactly = 0) { countryRepository.getAllCountries() }
            confirmVerified(countryRepository)
        }

    @Test
    fun `generateQuestions should return questions from local repository when generateQuestionsLocally is true and category is supported`() =
        runTest {
            val category = getCategory(
                id = ComparisonQuizApiImpl.supportedLocalCategories.random(),
                generateQuestionsLocally = true
            )
            val size = 10

            val questions = comparisonQuizApi.generateQuestions(
                category = category,
                size = size
            )

            // Check if no calls were made to the remote API
            assertThat(mockEngine.requestHistory).isEmpty()
            assertThat(questions).hasSize(size)

            coVerify(exactly = 1) { countryRepository.getAllCountries() }
            confirmVerified(countryRepository)
        }

    @Test
    fun `generateQuestions should return questions from remote API when generateQuestionsLocally is true and category is not supported`() = runTest {
        val category = getCategory(
            id = "not_supported",
            generateQuestionsLocally = true
        )
        val size = 10

        val questions = comparisonQuizApi.generateQuestions(
            category = category,
            size = size
        )

        assertThat(mockEngine.requestHistory).hasSize(1)
        assertThat(questions).hasSize(size)

        coVerify(exactly = 0) { countryRepository.getAllCountries() }
        confirmVerified(countryRepository)
    }

    private fun getCategory(
        id: String = "test",
        generateQuestionsLocally: Boolean = false
    ) = ComparisonQuizCategory(
        id = id,
        name = "test".toUiText(),
        image = "",
        description = "",
        questionDescription = ComparisonQuizCategory.QuestionDescription(
            greater = "greater",
            less = "less"
        ),
        formatType = NumberFormatType.DEFAULT,
        generateQuestionsLocally = generateQuestionsLocally
    )

    private fun mockLocalCountries() {
        val countries = List(100) {
            Country(
                countryCode = "code$it",
                countryName = "name$it",
                capital = "capital$it",
                population = it.toLong(),
                area = it.toDouble(),
                continent = Continent.from("Europe"),
                flagImage = URI.create("flag$it"),
                difficulty = QuestionDifficulty.random()
            )
        }
        coEvery { countryRepository.getAllCountries() } returns countries
    }
}
