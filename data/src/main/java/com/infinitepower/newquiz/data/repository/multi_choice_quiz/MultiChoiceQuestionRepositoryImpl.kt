package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.infinitepower.newquiz.core.common.dataStore.MultiChoiceQuestionDataStoreCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.MultiChoiceQuestionDataStoreManager
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.opentdb.OpenTDBQuestionResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultiChoiceQuestionRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    @MultiChoiceQuestionDataStoreManager private val settingsDataStoreManager: DataStoreManager
) : MultiChoiceQuestionRepository {
    private val random = SecureRandom()

    override suspend fun getRandomQuestions(
        amount: Int,
        category: Int?
    ): List<MultiChoiceQuestion> = withContext(Dispatchers.IO) {
        val openTDBResults = getOpenTDBResponse(amount, category).results

        val questions = openTDBResults.map { result ->
            async(Dispatchers.IO) {
                result.decodeResultToQuestion(id = random.nextInt())
            }
        }

        questions.awaitAll()
    }

    private suspend fun getOpenTDBResponse(
        amount: Int,
        category: Int?
    ): OpenTDBQuestionResponse {
        val response: HttpResponse = client.request("https://opentdb.com/api.php") {
            method = HttpMethod.Get
            parameter("encode", "base64")
            parameter("amount", amount)
            if (category != null) parameter("category", category)

            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val textResponse = response.bodyAsText()
        return Json.decodeFromString(textResponse)
    }

    override fun getRecentCategories(): Flow<List<MultiChoiceQuestionCategory>> =
        settingsDataStoreManager
            .getPreferenceFlow(MultiChoiceQuestionDataStoreCommon.RecentCategories)
            .map { recentCategoriesStr ->
                val categoryIds = Json.decodeFromString<List<Int>>(recentCategoriesStr)

                multiChoiceQuestionCategories.filter { it.id in categoryIds }
            }

    override suspend fun addCategoryToRecent(category: Int) {
        val recentCategoriesStr = settingsDataStoreManager.getPreference(MultiChoiceQuestionDataStoreCommon.RecentCategories)
        val categoryIds = Json.decodeFromString<List<Int>>(recentCategoriesStr)

        val newCategoriesIds = categoryIds
            .toMutableList()
            .apply {
                // If the category to add is in the recent it's not necessary
                // to add the category, so return
                if (category in this) return

                if (size >= 3) removeLast()
                add(0, category)
            }

        val newCategoriesIdsStr = Json.encodeToString(newCategoriesIds)

        settingsDataStoreManager.editPreference(
            key = MultiChoiceQuestionDataStoreCommon.RecentCategories.key,
            newValue = newCategoriesIdsStr
        )
    }
}