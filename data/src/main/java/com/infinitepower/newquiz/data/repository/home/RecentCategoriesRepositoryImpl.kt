package com.infinitepower.newquiz.data.repository.home

import com.infinitepower.newquiz.core.common.dataStore.RecentCategoryDataStoreCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.dataStore.manager.PreferenceRequest
import com.infinitepower.newquiz.core.di.RecentCategoriesDataStoreManager
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.domain.repository.home.HomeCategories
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentCategoriesRepositoryImpl @Inject constructor(
    @RecentCategoriesDataStoreManager private val recentCategoriesDataStoreManager: DataStoreManager
) : RecentCategoriesRepository {
    override fun getMultiChoiceCategories(): Flow<HomeCategories<MultiChoiceCategory>> = recentCategoriesDataStoreManager
        .getPreferenceFlow(RecentCategoryDataStoreCommon.MultiChoice)
        .map { recentCategoriesIds ->
            val allCategories = getAllCategories(isInternetAvailable = true)

            val recentCategories = getRecentCategories(
                recentCategories = recentCategoriesIds.mapNotNull { id ->
                    allCategories.find { it.key == id }
                },
                allCategories = allCategories,
                isInternetAvailable = true
            )

            val otherCategories = allCategories - recentCategories.toSet()

            HomeCategories(
                recentCategories = recentCategories,
                otherCategories = otherCategories
            )
        }

    private fun getAllCategories(
        isInternetAvailable: Boolean
    ): List<MultiChoiceCategory> {
        // If there is no internet, we make the categories that don't require internet connection
        // in the top of the list
        return if (isInternetAvailable) {
            multiChoiceQuestionCategories
        } else {
            multiChoiceQuestionCategories.sortedBy { it.requireInternetConnection }
        }
    }

    private fun getRecentCategories(
        recentCategories: List<MultiChoiceCategory>,
        allCategories: List<MultiChoiceCategory>,
        isInternetAvailable: Boolean
    ): List<MultiChoiceCategory> {
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
        addCategory(category.key, RecentCategoryDataStoreCommon.MultiChoice)
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