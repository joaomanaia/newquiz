package com.infinitepower.newquiz.data

import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.toUiText
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeComparisonQuizRepositoryImpl @Inject constructor() : ComparisonQuizRepository {
    override fun getCategories(): List<ComparisonQuizCategory> {
        return listOf(
            ComparisonQuizCategory(
                id = "1",
                name = "Category 1".toUiText(),
                description = "Description 1",
                image = "",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "Greater",
                    less = "Less"
                ),
                formatType = ComparisonQuizFormatType.Number
            )
        )
    }

    override fun getQuestions(category: ComparisonQuizCategory): FlowResource<List<ComparisonQuizItem>> {
        return emptyFlow()
    }

    override fun getHighestPosition(): FlowResource<Int> {
        return emptyFlow()
    }

    override suspend fun saveHighestPosition(position: Int) {
    }
}