package com.infinitepower.newquiz.domain.repository.home

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory
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

typealias HomeCategoriesFlow <T> = Flow<HomeCategories<T>>

interface RecentCategoriesRepository {
    fun getMultiChoiceCategories(): HomeCategoriesFlow<MultiChoiceCategory>

    fun getWordleCategories(): HomeCategoriesFlow<WordleCategory>

    fun getComparisonCategories(): HomeCategoriesFlow<ComparisonQuizCategory>

    suspend fun addMultiChoiceCategory(category: MultiChoiceBaseCategory)

    suspend fun addWordleCategory(categoryId: String)

    suspend fun addComparisonCategory(categoryId: String)

    suspend fun cleanAllSavedCategories()
}