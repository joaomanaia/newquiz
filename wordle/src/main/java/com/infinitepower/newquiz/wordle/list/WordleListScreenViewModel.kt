package com.infinitepower.newquiz.wordle.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WordleListScreenViewModel @Inject constructor(
    recentCategoriesRepository: RecentCategoriesRepository,
    networkStatusTracker: NetworkStatusTracker
) : ViewModel() {
    val uiState = combine(
        recentCategoriesRepository.getWordleCategories(
            isInternetAvailable = networkStatusTracker.isCurrentlyConnected()
        ),
        recentCategoriesRepository.getShowCategoryConnectionInfoFlow()
    ) { recentCategories, showCategoryConnectionInfo ->
        WordleListUiState(
            homeCategories = recentCategories,
            internetConnectionAvailable = networkStatusTracker.isCurrentlyConnected(),
            showCategoryConnectionInfo = showCategoryConnectionInfo
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WordleListUiState()
    )
}