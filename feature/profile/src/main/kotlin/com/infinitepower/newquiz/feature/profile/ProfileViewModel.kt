package com.infinitepower.newquiz.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.user_services.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
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
        val dateTimeRange = state.selectedTimeRange.getNowDateTimeRange()

        state.copy(xpEarnedList = userService.getXpEarnedBy(dateTimeRange).toPersistentList())
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
                    currentState.copy(selectedTimeRange = event.timeRange)
                }
            }
        }
    }
}
