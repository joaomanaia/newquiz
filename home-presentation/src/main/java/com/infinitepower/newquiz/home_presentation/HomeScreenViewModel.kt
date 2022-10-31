package com.infinitepower.newquiz.home_presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
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

        viewModelScope.launch(Dispatchers.IO) {
            Firebase
                .remoteConfig
                .getString("recommended_home_game")
                .also { gameStr ->
                    if (gameStr.isBlank()) return@launch

                    val game = RecommendedHomeGame.valueOf(gameStr.uppercase())

                    _uiState.update { currentState ->
                        currentState.copy(recommendedHomeGame = game)
                    }
                }
        }
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