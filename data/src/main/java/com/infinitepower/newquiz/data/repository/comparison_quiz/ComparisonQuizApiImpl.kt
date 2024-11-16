package com.infinitepower.newquiz.data.repository.comparison_quiz

import android.util.Log
import com.infinitepower.newquiz.core.common.BaseApiUrls
import com.infinitepower.newquiz.domain.repository.CountryRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ComparisonQuizApiImpl @Inject constructor(
    private val client: HttpClient,
    private val countryRepository: CountryRepository
) : ComparisonQuizApi {
    companion object {
        private const val TAG = "ComparisonQuizApiImpl"

        private const val COUNTRY_POPULATION_CATEGORY_ID = "country-population"
        private const val COUNTRY_AREA_CATEGORY_ID = "country-area"

        internal val supportedLocalCategories = setOf(
            COUNTRY_POPULATION_CATEGORY_ID,
            COUNTRY_AREA_CATEGORY_ID
        )
    }

    override suspend fun generateQuestions(
        category: ComparisonQuizCategory,
        size: Int,
        random: Random
    ): List<ComparisonQuizItemEntity> {
        return if (category.generateQuestionsLocally && category.id in supportedLocalCategories) {
            generateQuestionsLocally(
                category = category,
                size = size,
                random = random
            )
        } else {
            getQuestionsFromRemoteApi(
                category = category,
                size = size
            )
        }
    }

    private suspend fun getQuestionsFromRemoteApi(
        category: ComparisonQuizCategory,
        size: Int
    ): List<ComparisonQuizItemEntity> {
        Log.d(TAG, "Getting questions from remote API with category: ${category.id}")

        val apiUrl = "${BaseApiUrls.NEWQUIZ}/api/comparisonquiz/${category.id}"

        val response: HttpResponse = client.request(apiUrl) {
            headers {
                append(HttpHeaders.Accept, "application/json")
            }
            method = HttpMethod.Get
            parameter("size", size)
        }

        val textResponse = response.bodyAsText()
        return Json.decodeFromString(textResponse)
    }

    private suspend fun generateQuestionsLocally(
        category: ComparisonQuizCategory,
        size: Int,
        random: Random
    ): List<ComparisonQuizItemEntity> {
        return when (category.id) {
            COUNTRY_POPULATION_CATEGORY_ID, COUNTRY_AREA_CATEGORY_ID -> generateCountryQuestions(
                category = category,
                size = size,
                random = random
            )
            else -> throw UnsupportedOperationException("Category not supported: ${category.id}")
        }
    }

    private suspend fun generateCountryQuestions(
        category: ComparisonQuizCategory,
        size: Int,
        random: Random
    ): List<ComparisonQuizItemEntity> {
        Log.d(TAG, "Generating country questions locally with category: ${category.id}")

        val countries = countryRepository
            .getAllCountries()
            .shuffled(random)
            .take(size)

        return countries.map { country ->
            val value = when (category.id) {
                COUNTRY_POPULATION_CATEGORY_ID -> country.population.toDouble()
                COUNTRY_AREA_CATEGORY_ID -> country.area
                else -> throw UnsupportedOperationException("Category not supported: ${category.id}")
            }

            ComparisonQuizItemEntity(
                title = country.countryName,
                value = value,
                imgUrl = country.flagImage.toASCIIString()
            )
        }
    }
}
