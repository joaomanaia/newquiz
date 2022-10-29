package com.infinitepower.newquiz.online_services.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authUserRepository: AuthUserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val uid = authUserRepository.uid ?: return@launch
            val user = userRepository.getUserByUid(uid)

            _uiState.update { currentState ->
                currentState.copy(user = user)
            }
        }
    }
}