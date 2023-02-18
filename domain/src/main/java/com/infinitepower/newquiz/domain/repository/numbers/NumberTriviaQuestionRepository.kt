package com.infinitepower.newquiz.domain.repository.numbers

import androidx.annotation.IntRange
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.number.NumberTriviaQuestion
import com.infinitepower.newquiz.model.wordle.WordleWord
import kotlin.random.Random

interface NumberTriviaQuestionRepository {
    suspend fun generateRandomQuestions(
        size: Int = 1,
        @IntRange(from = 0) minNumber: Int,
        @IntRange(from = 0) maxNumber: Int,
        random: Random = Random
    ): List<NumberTriviaQuestion>

    suspend fun generateWordleQuestion(
        random: Random = Random
    ): WordleWord

    suspend fun generateMultiChoiceQuestion(
        size: Int = 1,
        random: Random = Random
    ): List<MultiChoiceQuestion>
}