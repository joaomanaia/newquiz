package com.infinitepower.newquiz.domain.repository.comparison_quiz

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizData

interface ComparisonQuizRepository {
    fun getCategories(): List<ComparisonQuizCategory>

    suspend fun getQuizData(
        category: ComparisonQuizCategory,
        comparisonMode: ComparisonModeByFirst
    ): FlowResource<ComparisonQuizData>

    fun getHighestPosition(): FlowResource<Int>

    suspend fun saveHighestPosition(position: Int)
}