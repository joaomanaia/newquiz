package com.infinitepower.newquiz.settings_presentation

sealed interface SettingsScreenUiEvent {
    object DownloadTranslationModel : SettingsScreenUiEvent

    object DeleteTranslationModel : SettingsScreenUiEvent

    object SignOut : SettingsScreenUiEvent

    data class EnableLoggingAnalytics(val enabled: Boolean) : SettingsScreenUiEvent

    object ClearHomeRecentCategories : SettingsScreenUiEvent
}
