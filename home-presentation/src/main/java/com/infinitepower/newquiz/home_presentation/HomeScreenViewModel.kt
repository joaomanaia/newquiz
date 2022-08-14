package com.infinitepower.newquiz.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @SettingsDataStoreManager private val dataStoreManager: DataStoreManager,
    private val authUserRepository: AuthUserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        dataStoreManager
            .getPreferenceFlow(SettingsCommon.ShowLoginCard)
            .onEach { showLoginCard ->
                _uiState.update { currentState ->
                    currentState.copy(showLoginCard = showLoginCard)
                }
            }.launchIn(viewModelScope)

        authUserRepository
            .isSignedInFlow
            .onEach { isLoggedIn ->
                _uiState.update { currentState ->
                    currentState.copy(isLoggedIn = isLoggedIn)
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            is HomeScreenUiEvent.DismissLoginCard -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreManager.editPreference(SettingsCommon.ShowLoginCard.key, false)
                }
            }
        }
    }
}