package com.infinitepower.newquiz.domain.repository.multi_choice_quiz

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import kotlinx.coroutines.flow.Flow

interface MultiChoiceQuestionRepository : MultiChoiceQuestionBaseRepository {

    fun getRecentCategories(): Flow<List<MultiChoiceQuestionCategory>>

    suspend fun addCategoryToRecent(category: Int)
}