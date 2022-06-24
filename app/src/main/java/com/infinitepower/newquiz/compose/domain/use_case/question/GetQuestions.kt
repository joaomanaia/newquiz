package com.infinitepower.newquiz.compose.domain.use_case.question

import com.infinitepower.newquiz.compose.core.generator.FakeQuestionGenerator
import com.infinitepower.newquiz.compose.data.remote.newquizapi.NewQuizApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetQuestions @Inject constructor(
    private val opentdbApi: NewQuizApi,
    private val fakeQuestionGenerator: FakeQuestionGenerator
) {
    suspend operator fun invoke(test: Boolean = false) =
        if (test) getTestQuestions() else opentdbApi.getQuestions()

    private suspend fun getTestQuestions() = fakeQuestionGenerator.getRandomQuestions(5)
}