package com.infinitepower.newquiz.comparison_quiz.data.comparison_quiz

import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FakeComparisonQuizRepositoryImpl(
    private val initialQuestions: List<ComparisonQuizItem> = emptyList(),
    private val initialCategories: List<ComparisonQuizCategory> = emptyList()
) : ComparisonQuizRepository {

    private val questions = mutableListOf<ComparisonQuizItem>()

    private val categories = mutableListOf<ComparisonQuizCategory>()

    private val highestPosition = MutableStateFlow(0)

    override fun getCategories(): List<ComparisonQuizCategory> = categories.toList()

    init {
        questions.addAll(initialQuestions)
        categories.addAll(initialCategories)
    }

    override fun getQuestions(category: ComparisonQuizCategory): FlowResource<List<ComparisonQuizItem>> {
        return flow {
            try {
                emit(Resource.Loading())

                emit(Resource.Success(initialQuestions))
            } catch (e: Exception) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error has occurred"))
            }
        }
    }

    override suspend fun getHighestPosition(categoryId: String): Int {
        return highestPosition.first()
    }

    override fun getHighestPositionFlow(categoryId: String): Flow<Int> {
        return highestPosition.map { it }
    }

    override suspend fun saveHighestPosition(categoryId: String, position: Int) {
        highestPosition.emit(position)
    }
}