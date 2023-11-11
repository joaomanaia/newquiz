package com.infinitepower.newquiz.domain.repository.comparison_quiz

import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import kotlinx.coroutines.flow.Flow

interface ComparisonQuizRepository {
    fun getCategories(): List<ComparisonQuizCategory>

    fun getQuestions(
        category: ComparisonQuizCategory
    ): FlowResource<List<ComparisonQuizItem>>

    /**
     * Get the highest position of the [category].
     */
    fun getHighestPositionFlow(
        category: ComparisonQuizCategory
    ): Flow<Int>

    suspend fun saveHighestPosition(
        category: ComparisonQuizCategory,
        position: Int
    )
}