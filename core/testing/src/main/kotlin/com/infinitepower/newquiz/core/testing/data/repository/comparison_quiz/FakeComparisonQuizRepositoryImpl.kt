package com.infinitepower.newquiz.core.testing.data.repository.comparison_quiz

import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.NumberFormatType
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.toUiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

private const val CATEGORIES_TO_GENERATE = 1
private const val QUESTIONS_TO_GENERATE = 10

@Singleton
class FakeComparisonQuizRepositoryImpl @Inject constructor() : ComparisonQuizRepository {
    private val highestPosition = MutableStateFlow<Map<String, Int>>(emptyMap())

    override fun getCategories(): List<ComparisonQuizCategory> {
        return List(CATEGORIES_TO_GENERATE) { id ->
            ComparisonQuizCategory(
                id = id.toString(),
                name = "Category $id".toUiText(),
                description = "Description $id",
                image = "",
                questionDescription = ComparisonQuizCategory.QuestionDescription(
                    greater = "Greater",
                    less = "Less"
                ),
                formatType = NumberFormatType.DEFAULT
            )
        }
    }

    override fun getQuestions(category: ComparisonQuizCategory): FlowResource<List<ComparisonQuizItem>> = flow {
        emit(Resource.Loading())

        val questions = List(QUESTIONS_TO_GENERATE) { id ->
            ComparisonQuizItem(
                title = "Question $id",
                value = Random.nextDouble(),
                imgUri = URI("")
            )
        }

        emit(Resource.Success(questions))
    }

    override suspend fun getHighestPosition(categoryId: String): Int {
        return highestPosition.value.getOrDefault(categoryId, 0)
    }

    override fun getHighestPositionFlow(categoryId: String): Flow<Int> {
        return highestPosition.map { it.getOrDefault(categoryId, 0)}
    }
}
