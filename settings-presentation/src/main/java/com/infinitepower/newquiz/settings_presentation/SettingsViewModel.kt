package com.infinitepower.newquiz.settings_presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.util.analytics.AnalyticsUtils
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import com.infinitepower.newquiz.settings_presentation.data.SettingsScreenPageData
import com.infinitepower.newquiz.settings_presentation.data.TranslatorModelState
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey
import com.infinitepower.newquiz.translation.TranslatorUtil
import com.infinitepower.newquiz.translation.work.DownloadTranslationModelWorker
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
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val translatorUtil: TranslatorUtil,
    private val authUserRepository: AuthUserRepository,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val recentCategoriesRepository: RecentCategoriesRepository,
    private val workManager: WorkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedStateHandle
            .getStateFlow(
                key = SettingsScreenNavArgs::screenKey.name,
                initialValue = SettingsScreenPageData.MainPage.key.value
            ).onEach { key ->
                _uiState.update { currentState ->
                    currentState.copy(screenKey = ScreenKey(key))
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { currentState ->
                currentState.copy(
                    translationModelState = if (translatorUtil.isModelDownloaded()) {
                        TranslatorModelState.Downloaded
                    } else {
                        TranslatorModelState.None
                    }
                )
            }
        }

        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    translatorTargetLanguages = translatorUtil.availableTargetLanguages
                )
            }
        }

        authUserRepository
            .isSignedInFlow
            .onEach { isSignedIn ->
                _uiState.update { currentState ->
                    currentState.copy(userIsSignedIn = isSignedIn)
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: SettingsScreenUiEvent) {
        when (event) {
            is SettingsScreenUiEvent.DeleteTranslationModel -> viewModelScope.launch(Dispatchers.IO) {
                translatorUtil.deleteModel()
            }
            is SettingsScreenUiEvent.DownloadTranslationModel -> downloadTranslationModel()
            is SettingsScreenUiEvent.SignOut -> authUserRepository.signOut()
            is SettingsScreenUiEvent.EnableLoggingAnalytics -> enableLoggingAnalytics(event.enabled)
            is SettingsScreenUiEvent.ClearHomeRecentCategories -> clearHomeRecentCategories()
        }
    }

    private fun downloadTranslationModel() = viewModelScope.launch {
        val targetLanguage = settingsDataStoreManager.getPreference(SettingsCommon.Translation.TargetLanguage)

        // Check if the target language is picked by the user
        if (targetLanguage.isEmpty()) {
            return@launch
        }

        val requireWifi = settingsDataStoreManager.getPreference(SettingsCommon.Translation.RequireWifi)
        val requireCharging = settingsDataStoreManager.getPreference(SettingsCommon.Translation.RequireCharging)

        // Start the download
        val workId = DownloadTranslationModelWorker.enqueueWork(
            workManager = workManager,
            targetLanguage = targetLanguage,
            requireWifi = requireWifi,
            requireCharging = requireCharging
        )

        // Get the worker status
        DownloadTranslationModelWorker
            .getWorkInfo(workManager, workId)
            .onEach { workInfo ->
                _uiState.update { currentState ->
                    currentState.copy(
                        translationModelState = when (workInfo.state) {
                            WorkInfo.State.SUCCEEDED -> TranslatorModelState.Downloaded
                            WorkInfo.State.RUNNING -> TranslatorModelState.Downloading
                            else -> TranslatorModelState.None
                        },
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun clearHomeRecentCategories() = viewModelScope.launch(Dispatchers.IO) {
        recentCategoriesRepository.cleanAllSavedCategories()
    }

    private fun enableLoggingAnalytics(enabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        val consentState = if (enabled) {
            DataAnalyticsConsentState.AGREED
        } else {
            DataAnalyticsConsentState.DISAGREED
        }

        settingsDataStoreManager.editPreference(
            key = SettingsCommon.DataAnalyticsConsent.key,
            newValue = consentState.name
        )

        // Enable general analytics
        val generalEnabled = settingsDataStoreManager.getPreference(SettingsCommon.GeneralAnalyticsEnabled)
        AnalyticsUtils.enableGeneralAnalytics(generalEnabled && enabled)

        // Enable crashlytics
        val crashlyticsEnabled = settingsDataStoreManager.getPreference(SettingsCommon.CrashlyticsEnabled)
        AnalyticsUtils.enableCrashlytics(crashlyticsEnabled && enabled)

        // Enable performance monitoring
        val performanceMonitoringEnabled = settingsDataStoreManager.getPreference(SettingsCommon.PerformanceMonitoringEnabled)
        AnalyticsUtils.enablePerformanceMonitoring(performanceMonitoringEnabled && enabled)
    }
}