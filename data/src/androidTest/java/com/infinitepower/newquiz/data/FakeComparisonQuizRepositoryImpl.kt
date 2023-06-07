package com.infinitepower.newquiz.data

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.game.ComparisonQuizData
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeComparisonQuizRepositoryImpl @Inject constructor() : ComparisonQuizRepository {
    override fun getCategories(): List<ComparisonQuizCategory> {
        return listOf(
            ComparisonQuizCategory(
                id = "1",
                title = "Category 1",
                description = "Description 1",
                imageUrl = "",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "Greater",
                    less = "Less"
                ),
                formatType = ComparisonQuizFormatType.Number
            )
        )
    }

    override suspend fun getQuizData(
        category: ComparisonQuizCategory,
        comparisonMode: ComparisonMode
    ): FlowResource<ComparisonQuizData> {
        return emptyFlow()
    }

    override fun getHighestPosition(): FlowResource<Int> {
        return emptyFlow()
    }

    override suspend fun saveHighestPosition(position: Int) {
    }
}