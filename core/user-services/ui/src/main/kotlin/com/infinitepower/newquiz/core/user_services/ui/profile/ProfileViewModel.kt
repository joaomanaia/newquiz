package com.infinitepower.newquiz.core.user_services.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.user_services.UserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userService: UserService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val user = userService.getUser()
            val xpThisWeek = userService.getXpEarnedInLastDuration()

            _uiState.update { currentState ->
                currentState.copy(
                    loading = false,
                    user = user,
                    xpEarnedLast7Days = xpThisWeek
                )
            }
        }
    }
}