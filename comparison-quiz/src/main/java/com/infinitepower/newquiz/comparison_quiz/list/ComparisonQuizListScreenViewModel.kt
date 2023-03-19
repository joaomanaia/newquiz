package com.infinitepower.newquiz.comparison_quiz.list

import androidx.lifecycle.ViewModel
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ComparisonQuizListScreenViewModel @Inject constructor(
    private val comparisonQuizRepository: ComparisonQuizRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComparisonQuizListScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(categories = comparisonQuizRepository.getCategories())
        }
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