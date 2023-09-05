package com.infinitepower.newquiz.comparison_quiz.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ComparisonQuizListScreenViewModel @Inject constructor(
    recentCategoriesRepository: RecentCategoriesRepository,
    networkStatusTracker: NetworkStatusTracker
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComparisonQuizListScreenUiState())

    val uiState = combine(
        _uiState,
        recentCategoriesRepository.getComparisonCategories(
            isInternetAvailable = networkStatusTracker.isCurrentlyConnected()
        )
    ) { uiState, recentCategories ->
        uiState.copy(
            homeCategories = recentCategories,
            internetConnectionAvailable = networkStatusTracker.isCurrentlyConnected()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ComparisonQuizListScreenUiState()
    )

    fun onEvent(event: ComparisonQuizListScreenUiEvent) {
        when (event) {
            is ComparisonQuizListScreenUiEvent.SelectMode -> {
                _uiState.update { currentState ->
                    currentState.copy(selectedMode = event.mode)
                }
            }
        }
    }
}