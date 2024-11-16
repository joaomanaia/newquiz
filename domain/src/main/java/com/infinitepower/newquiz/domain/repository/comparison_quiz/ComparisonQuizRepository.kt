package com.infinitepower.newquiz.domain.repository.comparison_quiz

import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

interface ComparisonQuizRepository {
    fun getCategories(): List<ComparisonQuizCategory>

    fun getCategoryById(id: String): ComparisonQuizCategory?

    suspend fun getQuestions(
        category: ComparisonQuizCategory,
        size: Int = 30,
        random: Random = Random
    ): List<ComparisonQuizItem>

    suspend fun getHighestPosition(
        categoryId: String
    ): Int

    /**
     * Get the highest position of the [category].
     */
    fun getHighestPositionFlow(
        categoryId: String
    ): Flow<Int>
}
