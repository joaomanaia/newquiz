package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.opentdb.OpenTDBQuestionResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultiChoiceQuestionRepositoryImpl @Inject constructor(
    private val client: HttpClient
) : MultiChoiceQuestionRepository {
    private val random = SecureRandom()

    override suspend fun getRandomQuestions(amount: Int): List<MultiChoiceQuestion> = withContext(Dispatchers.IO) {
        val openTDBResults = getOpenTDBResponse(amount).results

        val questions = openTDBResults.map { result ->
            async(Dispatchers.IO) {
                result.decodeResultToQuestion(id = random.nextInt())
            }
        }

        questions.awaitAll()
    }

    private suspend fun getOpenTDBResponse(amount: Int): OpenTDBQuestionResponse {
        val response: HttpResponse = client.request("https://opentdb.com/api.php") {
            method = HttpMethod.Get
            parameter("encode", "base64")
            parameter("amount", amount)
            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val textResponse = response.bodyAsText()
        return Json.decodeFromString(textResponse)
    }
}