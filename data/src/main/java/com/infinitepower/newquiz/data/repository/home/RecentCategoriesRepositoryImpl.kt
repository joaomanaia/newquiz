package com.infinitepower.newquiz.data.repository.home

import com.infinitepower.newquiz.core.datastore.PreferenceRequest
import com.infinitepower.newquiz.core.datastore.common.RecentCategoryDataStoreCommon
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.di.RecentCategoriesDataStoreManager
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.home.HomeCategories
import com.infinitepower.newquiz.domain.repository.home.HomeCategoriesFlow
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.category.ShowCategoryConnectionInfo
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentCategoriesRepositoryImpl @Inject constructor(
    @RecentCategoriesDataStoreManager private val recentCategoriesDataStoreManager: DataStoreManager,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val comparisonQuizRepository: ComparisonQuizRepository,
    private val remoteConfig: RemoteConfig
) : RecentCategoriesRepository {
    override fun getMultiChoiceCategories(
        isInternetAvailable: Boolean
    ): HomeCategoriesFlow<MultiChoiceCategory> = getHomeCategories(
        allCategories = multiChoiceQuestionCategories,
        request = RecentCategoryDataStoreCommon.MultiChoice,
        isInternetAvailable = isInternetAvailable
    )

    override fun getWordleCategories(
        isInternetAvailable: Boolean
    ): HomeCategoriesFlow<WordleCategory> = getHomeCategories(
        allCategories = WordleCategories.allCategories,
        request = RecentCategoryDataStoreCommon.Wordle,
        isInternetAvailable = isInternetAvailable
    )

    override fun getComparisonCategories(
        isInternetAvailable: Boolean
    ): HomeCategoriesFlow<ComparisonQuizCategory> = getHomeCategories(
        allCategories = comparisonQuizRepository.getCategories(),
        request = RecentCategoryDataStoreCommon.ComparisonQuiz,
        isInternetAvailable = isInternetAvailable
    )

    private fun <T : BaseCategory> getHomeCategories(
        allCategories: List<T>,
        request: PreferenceRequest<Set<String>>,
        isInternetAvailable: Boolean
    ): Flow<HomeCategories<T>> = getHomeCategories(
        allCategories = allCategories,
        recentCategoriesFlow = recentCategoriesDataStoreManager.getPreferenceFlow(request),
        hideOnlineCategoriesFlow = settingsDataStoreManager.getPreferenceFlow(SettingsCommon.HideOnlineCategories),
        isInternetAvailable = isInternetAvailable
    )

    internal fun <T : BaseCategory> getHomeCategories(
        allCategories: List<T>,
        recentCategoriesFlow: Flow<Set<String>>,
        hideOnlineCategoriesFlow: Flow<Boolean>,
        isInternetAvailable: Boolean
    ) = combine(
        recentCategoriesFlow,
        hideOnlineCategoriesFlow
    ) { recentCategoriesIds, hideOnlineCategories ->
        val shouldHideCategories = hideOnlineCategories && !isInternetAvailable

        val allCategoriesFiltered = if (shouldHideCategories) {
            allCategories.filter { !it.requireInternetConnection }
        } else {
            allCategories
        }

        getHomeBaseCategories(
            savedRecentCategoriesIds = recentCategoriesIds,
            allCategories = allCategoriesFiltered,
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
            recentCategories = recentCategories.toImmutableList(),
            otherCategories = otherCategories.sortByInternetConnection(isInternetAvailable).toImmutableList()
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
                // If there are no categories that don't require internet connection, we use all categories
                .ifEmpty { allCategories }
                .shuffled()
                .take(3)
        }
    }

    /**
     * Get the default value for the [ShowCategoryConnectionInfo] settings preference
     * from the remote config.
     */
    override fun getDefaultShowCategoryConnectionInfo(): ShowCategoryConnectionInfo {
        return remoteConfig.get(RemoteConfigValue.DEFAULT_SHOW_CATEGORY_CONNECTION_INFO)
    }

    override fun getShowCategoryConnectionInfoFlow(): Flow<ShowCategoryConnectionInfo> {
        val default = getDefaultShowCategoryConnectionInfo()

        return settingsDataStoreManager
            .getPreferenceFlow(SettingsCommon.CategoryConnectionInfoBadge(default))
            .map(ShowCategoryConnectionInfo::valueOf)    }

    override suspend fun addMultiChoiceCategory(categoryId: String) {
        addCategory(categoryId, RecentCategoryDataStoreCommon.MultiChoice)
    }

    override suspend fun addWordleCategory(categoryId: String) {
        addCategory(categoryId, RecentCategoryDataStoreCommon.Wordle)
    }

    override suspend fun addComparisonCategory(categoryId: String) {
        addCategory(categoryId, RecentCategoryDataStoreCommon.ComparisonQuiz)
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

    override suspend fun cleanAllSavedCategories() {
        recentCategoriesDataStoreManager.editPreference(
            key = RecentCategoryDataStoreCommon.MultiChoice.key,
            newValue = emptySet()
        )
        recentCategoriesDataStoreManager.editPreference(
            key = RecentCategoryDataStoreCommon.Wordle.key,
            newValue = emptySet()
        )
        recentCategoriesDataStoreManager.editPreference(
            key = RecentCategoryDataStoreCommon.ComparisonQuiz.key,
            newValue = emptySet()
        )
    }
}
