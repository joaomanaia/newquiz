package com.infinitepower.newquiz.domain.repository.home

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow

@Keep
data class HomeCategories <T> (
    val recentCategories: ImmutableList<T>,
    val otherCategories: ImmutableList<T>
)

fun <T> emptyHomeCategories() = HomeCategories<T>(
    recentCategories = persistentListOf(),
    otherCategories = persistentListOf()
)

typealias HomeCategoriesFlow <T> = Flow<HomeCategories<T>>

interface RecentCategoriesRepository {
    fun getMultiChoiceCategories(
        isInternetAvailable: Boolean
    ): HomeCategoriesFlow<MultiChoiceCategory>

    fun getWordleCategories(
        isInternetAvailable: Boolean
    ): HomeCategoriesFlow<WordleCategory>

    fun getComparisonCategories(
        isInternetAvailable: Boolean
    ): HomeCategoriesFlow<ComparisonQuizCategory>

    fun getDefaultShowCategoryConnectionInfo(): ShowCategoryConnectionInfo

    fun getShowCategoryConnectionInfoFlow(): Flow<ShowCategoryConnectionInfo>

    suspend fun addMultiChoiceCategory(categoryId: String)

    suspend fun addWordleCategory(categoryId: String)

    suspend fun addComparisonCategory(categoryId: String)

    suspend fun cleanAllSavedCategories()
}
