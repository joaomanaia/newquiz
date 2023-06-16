package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.opentdb.OpenTDBQuestionResponse
import com.infinitepower.newquiz.model.multi_choice_quiz.toQuestion
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

private const val OPENTDB_API_URL = "https://opentdb.com/api.php"

@Singleton
class MultiChoiceQuestionRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : MultiChoiceQuestionRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.Normal,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> = withContext(Dispatchers.IO) {
        val openTDBResults = getOpenTDBResponse(amount, category, difficulty).results

        val questions = openTDBResults.map { result ->
            async(Dispatchers.IO) {
                result
                    .decodeResultToQuestionEntity(id = random.nextInt())
                    .toQuestion()
            }
        }

        questions.awaitAll()
    }

    private suspend fun getOpenTDBResponse(
        amount: Int,
        category: MultiChoiceBaseCategory.Normal,
        difficulty: String?
    ): OpenTDBQuestionResponse {
        val response: HttpResponse = client.request(OPENTDB_API_URL) {
            method = HttpMethod.Get
            parameter("encode", "base64")
            parameter("amount", amount)
            if (category.hasCategory) {
                parameter("category", category.categoryId)
            }
            if (difficulty != null) parameter("difficulty", difficulty)

            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val textResponse = response.bodyAsText()
        return Json.decodeFromString(textResponse)
    }
}