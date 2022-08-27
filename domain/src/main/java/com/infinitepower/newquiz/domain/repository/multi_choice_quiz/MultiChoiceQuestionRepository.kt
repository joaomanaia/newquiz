package com.infinitepower.newquiz.domain.repository.multi_choice_quiz

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import kotlinx.coroutines.flow.Flow

interface MultiChoiceQuestionRepository {
    suspend fun getRandomQuestions(
        amount: Int = 5,
        category: Int? = null,
        difficulty: String? = null,
    ): List<MultiChoiceQuestion>

    fun getRecentCategories(): Flow<List<MultiChoiceQuestionCategory>>

    suspend fun addCategoryToRecent(category: Int)
}