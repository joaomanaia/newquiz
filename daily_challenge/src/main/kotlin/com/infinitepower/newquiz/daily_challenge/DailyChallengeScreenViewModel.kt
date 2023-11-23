package com.infinitepower.newquiz.daily_challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.model.global_event.GameEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyChallengeScreenViewModel @Inject constructor(
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val comparisonQuizRepository: ComparisonQuizRepository,
    private val userService: UserService
) : ViewModel() {
    private val _uiState = MutableStateFlow(DailyChallengeScreenUiState())
    val uiState = combine(
        _uiState,
        dailyChallengeRepository.getAvailableTasksFlow()
    ) { uiState, tasks ->
        uiState.copy(tasks = tasks)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = DailyChallengeScreenUiState()
    )

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    comparisonQuizCategories = comparisonQuizRepository.getCategories(),
                    userAvailable = userService.userAvailable()
                )
            }
        }
    }

    fun onEvent(event: DailyChallengeScreenUiEvent) {
        when (event) {
            is DailyChallengeScreenUiEvent.OnClaimTaskClick -> claimTask(event.taskType)
        }
    }

    private fun claimTask(taskType: GameEvent) {
        viewModelScope.launch {
            dailyChallengeRepository.claimTask(taskType)
        }
    }
}