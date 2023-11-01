package com.infinitepower.newquiz.settings_presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.translation.TranslatorModelState
import com.infinitepower.newquiz.core.translation.TranslatorUtil
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.model.DataAnalyticsConsentState
import com.infinitepower.newquiz.online_services.domain.user.auth.AuthUserRepository
import com.infinitepower.newquiz.settings_presentation.data.SettingsScreenPageData
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                val translationModelState = if (translatorUtil.isModelDownloaded()) {
                    TranslatorModelState.Downloaded
                } else {
                    TranslatorModelState.None
                }


                currentState.copy(
                    translatorAvailable = translatorUtil.isTranslatorAvailable,
                    translationModelState = translationModelState,
                    translatorTargetLanguages = translatorUtil.availableTargetLanguages,
                    defaultShowCategoryConnectionInfo = recentCategoriesRepository.getDefaultShowCategoryConnectionInfo()
                )
            }
        }

        combine(
            savedStateHandle.getStateFlow(
                key = SettingsScreenNavArgs::screenKey.name,
                initialValue = SettingsScreenPageData.MainPage.key.value
            ),
            authUserRepository.isSignedInFlow,
            settingsDataStoreManager.getPreferenceFlow(
                SettingsCommon.Translation.TargetLanguage
            )
        ) { screenKey, isSignedIn, targetLanguage ->
            Triple(screenKey, isSignedIn, targetLanguage)
        }.onEach { (screenKey, isSignedIn, targetLanguage) ->
            _uiState.update { currentState ->
                currentState.copy(
                    screenKey = ScreenKey(screenKey),
                    userIsSignedIn = isSignedIn,
                    translatorTargetLanguage = targetLanguage
                )
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

        translatorUtil.downloadModel(
            targetLanguage = targetLanguage,
            requireWifi = requireWifi,
            requireCharging = requireCharging
        ).onEach { downloadState ->
            _uiState.update { currentState ->
                currentState.copy(
                    translationModelState = downloadState
                )
            }
        }.catch { exception ->
            exception.printStackTrace()
            _uiState.update { currentState ->
                currentState.copy(
                    translationModelState = TranslatorModelState.None
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
        analyticsHelper.setGeneralAnalyticsEnabled(generalEnabled && enabled)

        // Enable crashlytics
        val crashlyticsEnabled = settingsDataStoreManager.getPreference(SettingsCommon.CrashlyticsEnabled)
        analyticsHelper.setCrashlyticsEnabled(crashlyticsEnabled && enabled)

        // Enable performance monitoring
        val performanceMonitoringEnabled = settingsDataStoreManager.getPreference(SettingsCommon.PerformanceMonitoringEnabled)
        analyticsHelper.setPerformanceEnabled(performanceMonitoringEnabled && enabled)
    }
}