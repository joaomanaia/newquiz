package com.infinitepower.newquiz.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.theme.AnimationsEnabled
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    authUserRepository: AuthUserRepository,
    dailyChallengeRepository: DailyChallengeRepository,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {
    private val animationsEnabledFlow = combine(
        settingsDataStoreManager.getPreferenceFlow(SettingsCommon.GlobalAnimationsEnabled),
        settingsDataStoreManager.getPreferenceFlow(SettingsCommon.WordleAnimationsEnabled),
        settingsDataStoreManager.getPreferenceFlow(SettingsCommon.MultiChoiceAnimationsEnabled),
    ) { globalAnimationsEnabled, wordleAnimationsEnabled, multiChoiceAnimationsEnabled ->
        AnimationsEnabled(
            global = globalAnimationsEnabled,
            wordle = wordleAnimationsEnabled && globalAnimationsEnabled,
            multiChoice = multiChoiceAnimationsEnabled && globalAnimationsEnabled
        )
    }

    val uiState: StateFlow<MainScreenUiState> = combine(
        authUserRepository.isSignedInFlow,
        animationsEnabledFlow,
        settingsDataStoreManager.getPreferenceFlow(SettingsCommon.ShowLoginCard),
        settingsDataStoreManager.getPreferenceFlow(SettingsCommon.DataAnalyticsConsent),
        dailyChallengeRepository.getClaimableTasksCountFlow()
    ) { signedIn, animationsEnabled, showLoginCard, dataAnalyticsDialogConsent, dailyChallengeClaimableCount ->
        MainScreenUiState(
            loading = false,
            signedIn = signedIn,
            animationsEnabled = animationsEnabled,
            settingsShowLoginCard = showLoginCard,
            dialogConsent = DataAnalyticsConsentState.valueOf(dataAnalyticsDialogConsent),
            dailyChallengeClaimableCount = dailyChallengeClaimableCount
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainScreenUiState(),
        started = SharingStarted.WhileSubscribed(5_000)
    )

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

            analyticsHelper.enableAll(agreed)
        }
    }
}
