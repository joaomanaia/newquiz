package com.infinitepower.newquiz.domain.repository.comparison_quiz

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.game.ComparisonQuizData
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory

interface ComparisonQuizRepository {
    fun getCategories(): List<ComparisonQuizCategory>

    suspend fun getQuizData(
        category: ComparisonQuizCategory,
        comparisonMode: ComparisonMode
    ): FlowResource<ComparisonQuizData>

    fun getHighestPosition(): FlowResource<Int>

    suspend fun saveHighestPosition(position: Int)
}