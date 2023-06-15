package com.infinitepower.newquiz.data.repository.home

import com.infinitepower.newquiz.core.common.dataStore.RecentCategoryDataStoreCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.dataStore.manager.PreferenceRequest
import com.infinitepower.newquiz.core.di.RecentCategoriesDataStoreManager
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.domain.repository.home.HomeCategories
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentCategoriesRepositoryImpl @Inject constructor(
    @RecentCategoriesDataStoreManager private val recentCategoriesDataStoreManager: DataStoreManager
) : RecentCategoriesRepository {
    override fun getMultiChoiceCategories(): Flow<HomeCategories<MultiChoiceCategory>> = getHomeCategories(
        allCategories = multiChoiceQuestionCategories,
        request = RecentCategoryDataStoreCommon.MultiChoice,
        isInternetAvailable = true
    )

    override fun getWordleCategories(): Flow<HomeCategories<WordleCategory>> = getHomeCategories(
        allCategories = WordleCategories.allCategories,
        request = RecentCategoryDataStoreCommon.Wordle,
        isInternetAvailable = true
    )

    private fun <T : BaseCategory> getHomeCategories(
        allCategories: List<T>,
        request: PreferenceRequest<Set<String>>,
        isInternetAvailable: Boolean
    ): Flow<HomeCategories<T>> = recentCategoriesDataStoreManager
        .getPreferenceFlow(request)
        .map { recentCategoriesIds ->
            getHomeBaseCategories(
                savedRecentCategoriesIds = recentCategoriesIds,
                allCategories = allCategories,
                isInternetAvailable = isInternetAvailable
            )
        }

    private fun <T : BaseCategory> getHomeBaseCategories(
        savedRecentCategoriesIds: Set<String>,
        allCategories: List<T>,
        isInternetAvailable: Boolean
    ): HomeCategories<T> {
        val savedRecentCategories = savedRecentCategoriesIds.mapNotNull { id ->
            allCategories.find { it.id == id }
        }

        val recentCategories = getRecentCategories(
            recentCategories = savedRecentCategories,
            allCategories = allCategories,
            isInternetAvailable = isInternetAvailable
        )

        val otherCategories = allCategories - recentCategories.toSet()

        return HomeCategories(
            recentCategories = recentCategories,
            otherCategories = otherCategories.sortByInternetConnection(isInternetAvailable)
        )
    }

    /**
     * If there is internet available, we return all the categories normally,
     * but if there is no internet, we make the categories that don't require internet connection
     * in the top of the list.
     */
    private fun <T : BaseCategory> List<T>.sortByInternetConnection(
        isInternetAvailable: Boolean
    ): List<T> = if (isInternetAvailable) this else sortedBy { it.requireInternetConnection }

    private fun <T : BaseCategory> getRecentCategories(
        recentCategories: List<T>,
        allCategories: List<T>,
        isInternetAvailable: Boolean
    ): List<T> {
        // When there are recent categories, we return them
        return recentCategories.ifEmpty {
            // If there are no recent categories, we take 3 random ones,
            // So we don't show all categories initially
            allCategories
                // If there is no internet, we only show the categories that don't require internet connection
                .filter { !it.requireInternetConnection || isInternetAvailable }
                .shuffled()
                .take(3)
        }
    }

    override suspend fun addMultiChoiceCategory(category: MultiChoiceBaseCategory) {
        addCategory(category.id, RecentCategoryDataStoreCommon.MultiChoice)
    }

    override suspend fun addWordleCategory(categoryId: String) {
        addCategory(categoryId, RecentCategoryDataStoreCommon.Wordle)
    }

    override suspend fun cleanMultiChoiceCategories() {
        recentCategoriesDataStoreManager.editPreference(
            key = RecentCategoryDataStoreCommon.MultiChoice.key,
            newValue = emptySet()
        )
    }

    override suspend fun cleanAll() {
        cleanMultiChoiceCategories()
    }

    private suspend fun addCategory(
        id: String,
        preferenceRequest: PreferenceRequest<Set<String>>
    ) {
        val recentCategories = recentCategoriesDataStoreManager.getPreference(preferenceRequest)

        val newCategoriesIds = recentCategories
            .toMutableSet()
            .apply {
                // If the category to add is in the recent it's not necessary
                // to add the category, so return
                if (id in this) return

                if (size >= 3) remove(last())

                add(id)
            }.toSet()

        recentCategoriesDataStoreManager.editPreference(
            key = preferenceRequest.key,
            newValue = newCategoriesIds
        )
    }
}