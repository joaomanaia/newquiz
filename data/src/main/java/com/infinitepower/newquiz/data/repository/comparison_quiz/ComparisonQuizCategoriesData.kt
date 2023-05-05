package com.infinitepower.newquiz.data.repository.comparison_quiz

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object ComparisonQuizCategoriesData {
    private val categoriesCache: MutableList<ComparisonQuizCategory> = mutableListOf()

    fun getCategories(): List<ComparisonQuizCategory> {
        if (categoriesCache.isEmpty()) {
            val categoriesStr = Firebase.remoteConfig.getString("comparison_quiz_categories")

            categoriesCache.addAll(Json.decodeFromString(categoriesStr))
        }

        return categoriesCache
    }
}