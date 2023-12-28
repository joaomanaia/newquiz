package com.infinitepower.newquiz.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.theme.AnimationsEnabled
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.domain.repository.daily_challenge.DailyChallengeRepository
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
    dailyChallengeRepository: DailyChallengeRepository,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val analyticsHelper: AnalyticsHelper,
    private val userService: UserService
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
        animationsEnabledFlow,
        analyticsHelper.showDataAnalyticsConsentDialog,
        dailyChallengeRepository.getClaimableTasksCountFlow(),
        userService.getUserDiamondsFlow()
    ) { animationsEnabled, showDataAnalyticsConsentDialog, dailyChallengeClaimableCount, userDiamonds ->
        MainScreenUiState(
            loading = false,
            animationsEnabled = animationsEnabled,
            showDataAnalyticsConsentDialog = showDataAnalyticsConsentDialog,
            dailyChallengeClaimableCount = dailyChallengeClaimableCount,
            userDiamonds = userDiamonds
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainScreenUiState(),
        started = SharingStarted.WhileSubscribed(UI_STATE_STOP_TIMEOUT)
    )

    companion object {
        private const val UI_STATE_STOP_TIMEOUT = 5_000L
    }

    fun onEvent(event: MainScreenUiEvent) {
        when (event) {
            is MainScreenUiEvent.OnDataAnalyticsConsentClick -> viewModelScope.launch(Dispatchers.IO) {
                analyticsHelper.updateDataConsent(event.agreed)
            }
        }
    }
}
