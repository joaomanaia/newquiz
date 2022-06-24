package com.infinitepower.newquiz.compose.data.repository.question

import com.infinitepower.newquiz.compose.data.local.quiz.QuestionLanguage
import com.infinitepower.newquiz.compose.data.remote.newquizapi.NewQuizApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlin.random.Random
import kotlin.random.nextInt

class FakeQuestionApiImpl : NewQuizApi {
    override suspend fun getQuestions() =  (1..5).asFlow().map {
        Question(
            id = it,
            description = "Era uma vez uma pergunta $it",
            category = "a",
            correctAns = Random.nextInt(0..4),
            difficulty = "",
            lang = QuestionLanguage.EN.name,
            type = "multiple",
            options = listOf(
                "Resposta A",
                "Resposta B",
                "Resposta C",
                "Resposta D",
            )
        )
    }.toList()
}