package com.infinitepower.newquiz.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @SettingsDataStoreManager private val dataStoreManager: DataStoreManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreManager
                .getPreferenceFlow(SettingsCommon.ShowLoginCard)
                .collect { showLoginCard ->
                    _uiState.update { currentState ->
                        currentState.copy(showLoginCard = showLoginCard)
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