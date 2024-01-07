package com.infinitepower.newquiz.core.user_services.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.user_services.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userService: UserService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState = _uiState.map { state ->
        state.copy(
            xpEarnedLast7Days = userService.getXpEarnedBy(state.timeRange)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ProfileScreenUiState()
    )

    init {
        viewModelScope.launch {
            val user = userService.getUser()

            _uiState.update { currentState ->
                currentState.copy(
                    loading = false,
                    user = user
                )
            }
        }
    }

    fun onEvent(event: ProfileScreenUiEvent) {
        when (event) {
            is ProfileScreenUiEvent.OnFilterByTimeRangeClick -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        timeRangeIndex = event.timeRangeIndex
                    )
                }
            }
        }
    }
}
