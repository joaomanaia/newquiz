package com.infinitepower.newquiz.settings_presentation

sealed interface SettingsScreenUiEvent {
    data object DownloadTranslationModel : SettingsScreenUiEvent

    object DeleteTranslationModel : SettingsScreenUiEvent

    data class EnableLoggingAnalytics(val enabled: Boolean) : SettingsScreenUiEvent

    object ClearHomeRecentCategories : SettingsScreenUiEvent
}
