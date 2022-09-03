package com.infinitepower.newquiz.settings_presentation

sealed class SettingsScreenUiEvent {
    object DownloadTranslationModel : SettingsScreenUiEvent()

    object DeleteTranslationModel : SettingsScreenUiEvent()
}
