package com.infinitepower.newquiz.data.repository.comparison_quiz

import com.infinitepower.newquiz.core.common.BaseApiUrls
import com.infinitepower.newquiz.core.database.dao.ComparisonQuizDao
import com.infinitepower.newquiz.core.database.model.ComparisonQuizHighestPosition
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategoryEntity
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import com.infinitepower.newquiz.model.comparison_quiz.toComparisonQuizItem
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComparisonQuizRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    private val remoteConfig: RemoteConfig,
    private val comparisonQuizDao: ComparisonQuizDao
) : ComparisonQuizRepository {
    private val categoriesCache: MutableList<ComparisonQuizCategory> = mutableListOf()

    override fun getCategories(): List<ComparisonQuizCategory> {
        if (categoriesCache.isEmpty()) {
            val categoriesStr = remoteConfig.getString("comparison_quiz_categories")
            val categoriesEntity: List<ComparisonQuizCategoryEntity> = Json.decodeFromString(categoriesStr)
            val categories = categoriesEntity.map(ComparisonQuizCategoryEntity::toModel)

            categoriesCache.addAll(categories)
        }

        return categoriesCache
    }

    override fun getQuestions(
        category: ComparisonQuizCategory
    ): FlowResource<List<ComparisonQuizItem>> = flow {
        try {
            emit(Resource.Loading())

            val apiUrl = "${BaseApiUrls.NEWQUIZ}/api/comparisonquiz/${category.id}"

            val response: HttpResponse = client.request(apiUrl) {
                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
            }

            val textResponse = response.bodyAsText()
            val entityQuestions: List<ComparisonQuizItemEntity> = Json.decodeFromString(textResponse)

            val questions = entityQuestions.map(ComparisonQuizItemEntity::toComparisonQuizItem)

            emit(Resource.Success(questions))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred while fetching comparison quiz data"))
        }
    }

    override fun getHighestPositionFlow(category: ComparisonQuizCategory): Flow<Int> = comparisonQuizDao
        .getHighestPosition(category.id)
        .map { it?.highestPosition ?: 0 }

    override suspend fun saveHighestPosition(category: ComparisonQuizCategory, position: Int) {
        val entity = ComparisonQuizHighestPosition(
            categoryId = category.id,
            highestPosition = position
        )

        comparisonQuizDao.upsert(entity)
    }
}
