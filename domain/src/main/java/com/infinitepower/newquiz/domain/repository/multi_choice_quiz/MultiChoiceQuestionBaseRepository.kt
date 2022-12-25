package com.infinitepower.newquiz.domain.repository.multi_choice_quiz

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import kotlin.random.Random

sealed interface MultiChoiceQuestionBaseRepository {
    suspend fun getRandomQuestions(
        amount: Int = 5,
        category: Int? = null,
        difficulty: String? = null,
        random: Random = Random
    ): List<MultiChoiceQuestion>
}