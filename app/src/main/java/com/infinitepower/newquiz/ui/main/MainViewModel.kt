package com.infinitepower.newquiz.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
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
class MainViewModel @Inject constructor(
    private val authUserRepository: AuthUserRepository,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        authUserRepository
            .isSignedInFlow
            .onEach { isSignedIn ->
                _uiState.update { currentState ->
                    currentState.copy(signedIn = isSignedIn)
                }
            }.launchIn(viewModelScope)

        settingsDataStoreManager
            .getPreferenceFlow(SettingsCommon.DataAnalyticsConsent)
            .onEach { strConsent ->
                _uiState.update { currentState ->
                    val consent = DataAnalyticsConsentState.valueOf(strConsent)

                    currentState.copy(dialogConsent = consent)
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: MainScreenUiEvent) {
        when (event) {
            is MainScreenUiEvent.OnAgreeDisagreeClick -> updateDataConsent(event.agreed)
        }
    }

    private fun updateDataConsent(agreed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val consentState = if (agreed) {
                DataAnalyticsConsentState.AGREED
            } else {
                DataAnalyticsConsentState.DISAGREED
            }

            settingsDataStoreManager.editPreference(
                key = SettingsCommon.DataAnalyticsConsent.key,
                newValue = consentState.name
            )

            settingsDataStoreManager.editPreference(
                key = SettingsCommon.DataAnalyticsCollectionEnabled.key,
                newValue = agreed
            )
        }
    }
}
