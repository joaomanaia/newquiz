package com.infinitepower.newquiz.domain.repository.comparison_quiz

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem

interface ComparisonQuizRepository {
    fun getCategories(): List<ComparisonQuizCategory>

    fun getQuestions(
        category: ComparisonQuizCategory
    ): FlowResource<List<ComparisonQuizItem>>

    fun getHighestPosition(): FlowResource<Int>

    suspend fun saveHighestPosition(position: Int)
}