package com.infinitepower.newquiz.compose.domain.use_case.question

import com.infinitepower.newquiz.compose.data.remote.opentdb.NewQuizApi
import com.infinitepower.newquiz.compose.model.quiz.Question
import com.infinitepower.newquiz.quiz.question.QuestionLanguage
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.random.nextInt

@Singleton
class GetQuestions @Inject constructor(
    private val opentdbApi: NewQuizApi
) {
    suspend operator fun invoke(test: Boolean = false) =
        if (test) getTestQuestions() else opentdbApi.getQuestions()

    private suspend fun getTestQuestions() = (1..5).asFlow().map {
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
                "Resposta D"
            )
        )
    }.toList()
}