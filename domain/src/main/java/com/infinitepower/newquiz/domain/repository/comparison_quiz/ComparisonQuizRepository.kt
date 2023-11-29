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

    suspend fun getHighestPosition(
        categoryId: String
    ): Int

    /**
     * Get the highest position of the [category].
     */
    fun getHighestPositionFlow(
        categoryId: String
    ): Flow<Int>

    suspend fun saveHighestPosition(
        categoryId: String,
        position: Int
    )
}