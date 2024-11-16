package com.infinitepower.newquiz.data.repository.comparison_quiz

import com.infinitepower.newquiz.core.database.dao.GameResultDao
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.data.util.mappers.comparisonquiz.toModel
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategoryEntity
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ComparisonQuizRepositoryImpl @Inject constructor(
    private val remoteConfig: RemoteConfig,
    private val gameResultDao: GameResultDao,
    private val comparisonQuizApi: ComparisonQuizApi
) : ComparisonQuizRepository {
    private val categoriesCache: MutableList<ComparisonQuizCategory> = mutableListOf()

    override fun getCategories(): List<ComparisonQuizCategory> {
        if (categoriesCache.isEmpty()) {
            val categoriesStr = remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_CATEGORIES)
            val categoriesEntity: List<ComparisonQuizCategoryEntity> =
                Json.decodeFromString(categoriesStr)
            val categories = categoriesEntity.map(ComparisonQuizCategoryEntity::toModel)

            categoriesCache.addAll(categories)
        }

        return categoriesCache
    }

    override fun getCategoryById(id: String): ComparisonQuizCategory? {
        return getCategories().find { it.id == id }
    }

    override suspend fun getQuestions(
        category: ComparisonQuizCategory,
        size: Int,
        random: Random
    ): List<ComparisonQuizItem> {
        val entityQuestions = comparisonQuizApi.generateQuestions(
            category = category,
            size = size,
            random = random
        )

        return entityQuestions.map(ComparisonQuizItemEntity::toModel)
    }

    override suspend fun getHighestPosition(categoryId: String): Int {
        return gameResultDao.getComparisonQuizHighestPosition(categoryId)
    }

    override fun getHighestPositionFlow(categoryId: String): Flow<Int> {
        return gameResultDao.getComparisonQuizHighestPositionFlow(categoryId)
    }
}
