package com.infinitepower.newquiz.online_services.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.online_services.domain.usecase.GetLocalUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getLocalUserUseCase: GetLocalUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getLocalUserUseCase()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(
                        user = res.data,
                        loading = res is Resource.Loading
                    )
                }
            }.launchIn(viewModelScope)
    }
}