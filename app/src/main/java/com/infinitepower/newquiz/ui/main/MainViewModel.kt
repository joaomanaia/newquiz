package com.infinitepower.newquiz.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.theme.AnimationsEnabled
import com.infinitepower.newquiz.core.util.analytics.AnalyticsUtils
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUserRepository: AuthUserRepository,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val dailyChallengeRepository: DailyChallengeRepository
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
            .getPreferenceFlow(SettingsCommon.ShowLoginCard)
            .onEach { showLoginCard ->
                _uiState.update { currentState ->
                    currentState.copy(settingsShowLoginCard = showLoginCard)
                }
            }.launchIn(viewModelScope)

        settingsDataStoreManager
            .getPreferenceFlow(SettingsCommon.DataAnalyticsConsent)
            .onEach { strConsent ->
                _uiState.update { currentState ->
                    val consent = DataAnalyticsConsentState.valueOf(strConsent)

                    currentState.copy(
                        dialogConsent = consent,
                        consentStateLoading = false
                    )
                }
            }.launchIn(viewModelScope)

        dailyChallengeRepository
            .getClaimableTasksCountFlow()
            .onEach { count ->
                _uiState.update { currentState ->
                    currentState.copy(dailyChallengeClaimableCount = count)
                }
            }.launchIn(viewModelScope)

        combine(
            settingsDataStoreManager.getPreferenceFlow(SettingsCommon.GlobalAnimationsEnabled),
            settingsDataStoreManager.getPreferenceFlow(SettingsCommon.WordleAnimationsEnabled),
        ) { globalAnimationsEnabled, wordleAnimationsEnabled ->
            AnimationsEnabled(
                global = globalAnimationsEnabled,
                wordle = wordleAnimationsEnabled && globalAnimationsEnabled
            )
        }.onEach { animationsEnabled ->
            _uiState.update { currentState ->
                currentState.copy(animationsEnabled = animationsEnabled)
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MainScreenUiEvent) {
        when (event) {
            is MainScreenUiEvent.OnAgreeDisagreeClick -> updateDataConsent(event.agreed)
            is MainScreenUiEvent.DismissLoginCard -> {
                viewModelScope.launch(Dispatchers.IO) {
                    settingsDataStoreManager.editPreference(SettingsCommon.ShowLoginCard.key, false)
                }
            }
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

            // Update all data analytics settings
            settingsDataStoreManager.editPreference(
                key = SettingsCommon.DataAnalyticsCollectionEnabled.key,
                newValue = agreed
            )

            settingsDataStoreManager.editPreference(
                key = SettingsCommon.GeneralAnalyticsEnabled.key,
                newValue = agreed
            )

            settingsDataStoreManager.editPreference(
                key = SettingsCommon.CrashlyticsEnabled.key,
                newValue = agreed
            )

            settingsDataStoreManager.editPreference(
                key = SettingsCommon.PerformanceMonitoringEnabled.key,
                newValue = agreed
            )

            AnalyticsUtils.enableAll(agreed)
        }
    }
}
