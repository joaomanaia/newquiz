package com.infinitepower.newquiz.core.testing.data.repository.numbers

import com.infinitepower.newquiz.domain.repository.numbers.NumberTriviaQuestionApi
import com.infinitepower.newquiz.model.number.NumberTriviaQuestionEntity
import com.infinitepower.newquiz.model.number.NumberTriviaQuestionsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeNumberTriviaQuestionApiImpl @Inject constructor() : NumberTriviaQuestionApi {
    override suspend fun getRandomQuestion(
        size: Int,
        minNumber: Int,
        maxNumber: Int
    ): NumberTriviaQuestionsEntity {
        val questions = List(size) {
            val randomNumber = (minNumber..maxNumber).random()

            NumberTriviaQuestionEntity(
                number = randomNumber,
                question = "What is $randomNumber?"
            )
        }

        return NumberTriviaQuestionsEntity(questions = questions)
    }
}
