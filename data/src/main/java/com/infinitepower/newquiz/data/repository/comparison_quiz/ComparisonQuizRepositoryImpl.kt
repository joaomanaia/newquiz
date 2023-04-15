package com.infinitepower.newquiz.data.repository.comparison_quiz

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.core.common.BaseApiUrls
import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.common.dataStore.ComparisonQuizDataStoreCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.ComparisonQuizDataStoreManager
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizData
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import com.infinitepower.newquiz.model.comparison_quiz.toComparisonQuizItem
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComparisonQuizRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    @ComparisonQuizDataStoreManager private val settingsDataStoreManager: DataStoreManager
) : ComparisonQuizRepository {
    private val remoteConfig = Firebase.remoteConfig

    override fun getCategories(): List<ComparisonQuizCategory> {
        val categoriesStr = remoteConfig.getString("comparison_quiz_categories")

        return Json.decodeFromString(categoriesStr)
    }

    override suspend fun getQuizData(
        category: ComparisonQuizCategory,
        comparisonMode: ComparisonModeByFirst
    ): FlowResource<ComparisonQuizData> = flow {
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

            val questionDescription = category.getQuestionDescription(comparisonMode)

            val quizData = ComparisonQuizData(
                questionDescription = questionDescription,
                questions = questions,
                comparisonMode = comparisonMode
            )

            emit(Resource.Success(quizData))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred while fetching comparison quiz data"))
        }
    }

    override fun getHighestPosition(): FlowResource<Int> = flow {
        try {
            emit(Resource.Loading())

            val highestPosition = settingsDataStoreManager
                .getPreferenceFlow(ComparisonQuizDataStoreCommon.HighestPosition)
                .map { value -> Resource.Success(value) }

            emitAll(highestPosition)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred while fetching highest position"))
        }
    }

    override suspend fun saveHighestPosition(position: Int) {
        settingsDataStoreManager.editPreference(ComparisonQuizDataStoreCommon.HighestPosition.key, position)
    }
}
