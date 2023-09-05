package com.infinitepower.newquiz.wordle.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WordleListScreenViewModel @Inject constructor(
    recentCategoriesRepository: RecentCategoriesRepository,
    networkStatusTracker: NetworkStatusTracker
) : ViewModel() {
    val uiState = recentCategoriesRepository.getWordleCategories(
        isInternetAvailable = networkStatusTracker.isCurrentlyConnected()
    ).map { recentCategories ->
        WordleListUiState(
            homeCategories = recentCategories,
            internetConnectionAvailable = networkStatusTracker.isCurrentlyConnected()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WordleListUiState()
    )
}