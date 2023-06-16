package com.infinitepower.newquiz.comparison_quiz.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ComparisonQuizListScreenViewModel @Inject constructor(
    private val recentCategoriesRepository: RecentCategoriesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComparisonQuizListScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        recentCategoriesRepository
            .getComparisonCategories()
            .onEach { homeCategories ->
                _uiState.update { currentState ->
                    currentState.copy(homeCategories = homeCategories)
                }
            }.launchIn(viewModelScope)
    }

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