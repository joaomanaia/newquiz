package com.infinitepower.newquiz.daily_challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.model.global_event.GameEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyChallengeScreenViewModel @Inject constructor(
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val comparisonQuizRepository: ComparisonQuizRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DailyChallengeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        dailyChallengeRepository
            .getAvailableTasksFlow()
            .onEach { tasks ->
                _uiState.update { currentState ->
                    currentState.copy(tasks = tasks)
                }
            }.launchIn(viewModelScope)

        _uiState.update { currentState ->
            currentState.copy(comparisonQuizCategories = comparisonQuizRepository.getCategories())
        }
    }

    fun onEvent(event: DailyChallengeScreenUiEvent) {
        when (event) {
            is DailyChallengeScreenUiEvent.OnClaimTaskClick -> claimTask(event.taskType)
        }
    }

    private fun claimTask(taskType: GameEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            dailyChallengeRepository.claimTask(taskType)
        }
    }
}