package com.infinitepower.newquiz.compose.data.repository.newquizapi

import com.infinitepower.newquiz.compose.data.remote.newquizapi.NewQuizApi
import com.infinitepower.newquiz.compose.model.question.Question
import com.infinitepower.newquiz.compose.util.decodeBase64Question
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class NewQuizApiImpl(
    private val client: HttpClient
) : NewQuizApi {
    override suspend fun getQuestions(): List<Question> {
        val response: HttpResponse = client.request("https://newquizapi.herokuapp.com/random_questions") {
            method = HttpMethod.Get
            parameter("questions_amount", "5")
            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val questions = response.body<List<Question>>().map { question ->
            question.decodeBase64Question()
        }
        return questions
    }
}