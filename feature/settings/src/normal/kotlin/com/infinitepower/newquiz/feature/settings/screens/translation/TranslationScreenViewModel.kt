package com.infinitepower.newquiz.feature.settings.screens.translation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.core.translation.TranslatorModelState
import com.infinitepower.newquiz.core.translation.TranslatorUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationScreenViewModel @Inject constructor(
    private val translatorUtil: TranslatorUtil,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TranslationScreenUiState())
    val uiState = combine(
        _uiState,
        settingsDataStoreManager.getPreferenceFlow(
            SettingsCommon.Translation.TargetLanguage
        )
    ) { uiState, targetLanguage ->
        uiState.copy(
            translatorTargetLanguage = targetLanguage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TranslationScreenUiState()
    )

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
                )
            }
        }
    }

    fun onEvent(event: TranslationScreenUiEvent) {
        when (event) {
            is TranslationScreenUiEvent.DownloadTranslationModel -> downloadTranslationModel()
            is TranslationScreenUiEvent.DeleteTranslationModel -> {
                viewModelScope.launch {
                    translatorUtil.deleteModel()
                }
            }
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
}
