package com.infinitepower.newquiz.domain.repository.multi_choice_quiz

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import kotlinx.coroutines.flow.Flow

interface MultiChoiceQuestionRepository : MultiChoiceQuestionBaseRepository<MultiChoiceBaseCategory.Normal> {

    fun getRecentCategories(): Flow<List<MultiChoiceCategory>>

    suspend fun addCategoryToRecent(category: MultiChoiceBaseCategory)

    fun isFlagQuizInCategories(): Boolean

    fun isCountryCapitalFlagQuizInCategories(): Boolean

    fun isLogoQuizInCategories(): Boolean
}