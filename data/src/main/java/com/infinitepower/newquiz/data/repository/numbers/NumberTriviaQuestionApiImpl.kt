package com.infinitepower.newquiz.data.repository.numbers

import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionApi
import com.infinitepower.newquiz.model.number.NumberTriviaQuestionsEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val NUMBERS_API_URL = "https://newquiz-app.vercel.app/api/numberTrivia/random"

@Singleton
class NumberTriviaQuestionApiImpl @Inject constructor(
    private val client: HttpClient
) : NumberTriviaQuestionApi {
    override suspend fun getRandomQuestion(
        size: Int,
        minNumber: Int,
        maxNumber: Int
    ): NumberTriviaQuestionsEntity {
        val response: HttpResponse = client.request(NUMBERS_API_URL) {
            parameter("size", size)
            parameter("min", minNumber)
            parameter("max", maxNumber)

            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }

        val textResponse = response.bodyAsText()
        return Json.decodeFromString(textResponse)
    }
}