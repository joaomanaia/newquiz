package com.infinitepower.newquiz.domain.repository.home

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import kotlinx.coroutines.flow.Flow

@Keep
data class HomeCategories <T> (
    val recentCategories: List<T>,
    val otherCategories: List<T>
)

fun <T> emptyHomeCategories() = HomeCategories<T>(
    recentCategories = emptyList(),
    otherCategories = emptyList()
)

interface RecentCategoriesRepository {
    fun getMultiChoiceCategories(): Flow<HomeCategories<MultiChoiceCategory>>

    suspend fun addMultiChoiceCategory(category: MultiChoiceBaseCategory)

    suspend fun cleanMultiChoiceCategories()

    suspend fun cleanAll()
}