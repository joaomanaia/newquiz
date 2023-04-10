package com.infinitepower.newquiz.data.repository.multi_choice_quiz

import com.google.firebase.ktx.Firebase
import com.google.firebase.perf.ktx.trace
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.core.common.dataStore.MultiChoiceQuestionDataStoreCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.MultiChoiceQuestionDataStoreManager
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.opentdb.OpenTDBQuestionResponse
import com.infinitepower.newquiz.model.multi_choice_quiz.toQuestion
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

private const val OPENTDB_API_URL = "https://opentdb.com/api.php"

@Singleton
class MultiChoiceQuestionRepositoryImpl @Inject constructor(
    private val client: HttpClient,
    @MultiChoiceQuestionDataStoreManager private val settingsDataStoreManager: DataStoreManager
) : MultiChoiceQuestionRepository {
    override suspend fun getRandomQuestions(
        amount: Int,
        category: MultiChoiceBaseCategory.Normal,
        difficulty: String?,
        random: Random
    ): List<MultiChoiceQuestion> = withContext(Dispatchers.IO) {
        trace(name = "GetOpenTDBQuestions") {
            val openTDBResults = getOpenTDBResponse(amount, category, difficulty).results

            val questions = openTDBResults.map { result ->
                async(Dispatchers.IO) {
                    result
                        .decodeResultToQuestionEntity(id = random.nextInt())
                        .toQuestion()
                }
            }

            questions.awaitAll()
        }
    }

    private suspend fun getOpenTDBResponse(
        amount: Int,
        category: MultiChoiceBaseCategory.Normal,
        difficulty: String?
    ): OpenTDBQuestionResponse {
        val response: HttpResponse = client.request(OPENTDB_API_URL) {
            method = HttpMethod.Get
            parameter("encode", "base64")
            parameter("amount", amount)
            if (category.hasCategory) {
                val categoryDB = multiChoiceQuestionCategories.find { it.key == category.categoryKey }
                if (categoryDB != null) parameter("category", categoryDB.id)
            }
            if (difficulty != null) parameter("difficulty", difficulty)

            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }
        val textResponse = response.bodyAsText()
        return Json.decodeFromString(textResponse)
    }

    override fun getRecentCategories(): Flow<List<MultiChoiceCategory>> =
        settingsDataStoreManager
            .getPreferenceFlow(MultiChoiceQuestionDataStoreCommon.RecentCategories)
            .map { recentCategories ->
                multiChoiceQuestionCategories.filter { it.key in recentCategories }
            }

    override suspend fun addCategoryToRecent(category: MultiChoiceBaseCategory) {
        val recentCategories = settingsDataStoreManager.getPreference(MultiChoiceQuestionDataStoreCommon.RecentCategories)

        val newCategoriesIds = recentCategories
            .toMutableSet()
            .apply {
                // If the category to add is in the recent it's not necessary
                // to add the category, so return
                if (category.key in this) return

                if (size >= 3) remove(last())

                add(category.key)
            }.toSet()

        settingsDataStoreManager.editPreference(
            key = MultiChoiceQuestionDataStoreCommon.RecentCategories.key,
            newValue = newCategoriesIds
        )
    }

    override fun isFlagQuizInCategories(): Boolean {
        return Firebase.remoteConfig.getBoolean("show_flag_quiz_in_categories")
    }

    override fun isCountryCapitalFlagQuizInCategories(): Boolean {
        return Firebase.remoteConfig.getBoolean("show_country_capital_flag_quiz_in_categories")
    }

    override fun isLogoQuizInCategories(): Boolean {
        return Firebase.remoteConfig.getBoolean("show_logo_quiz_in_categories")
    }
}