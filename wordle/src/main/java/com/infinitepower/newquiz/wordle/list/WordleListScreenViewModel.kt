package com.infinitepower.newquiz.wordle.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class WordleListScreenViewModel @Inject constructor(
    private val recentCategoriesRepository: RecentCategoriesRepository,
    private val networkStatusTracker: NetworkStatusTracker
) : ViewModel() {
    private val _uiState = MutableStateFlow(WordleListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        networkStatusTracker
            .networkStatus
            .onEach { status ->
                _uiState.update { currentState ->
                    currentState.copy(internetConnectionAvailable = status.isAvailable())
                }
            }.flatMapLatest { status ->
                recentCategoriesRepository.getWordleCategories(isInternetAvailable = status.isAvailable())
            }.onEach { homeCategories ->
                _uiState.update { currentState ->
                    currentState.copy(homeCategories = homeCategories)
                }
            }.launchIn(viewModelScope)
    }
}