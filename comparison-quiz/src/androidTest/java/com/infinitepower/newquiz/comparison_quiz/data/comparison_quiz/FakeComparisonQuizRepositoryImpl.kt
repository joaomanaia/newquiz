package com.infinitepower.newquiz.comparison_quiz.data.comparison_quiz

import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
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

    override fun getHighestPosition(): FlowResource<Int> = flow {
        try {
            emit(Resource.Loading())

            val highestPositionFlow = highestPosition.map { position -> Resource.Success(position) }

            emitAll(highestPositionFlow)
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error has occurred"))
        }
    }

    override suspend fun saveHighestPosition(position: Int) {
        highestPosition.emit(position)
    }
}