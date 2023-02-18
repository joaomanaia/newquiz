package com.infinitepower.newquiz.domain.repository.numbers

import androidx.annotation.IntRange
import com.infinitepower.newquiz.model.number.NumberTriviaQuestionsEntity

interface NumberTriviaQuestionApi {
    suspend fun getRandomQuestion(
        size: Int,
        @IntRange(from = 0) minNumber: Int,
        @IntRange(from = 0) maxNumber: Int
    ): NumberTriviaQuestionsEntity
}