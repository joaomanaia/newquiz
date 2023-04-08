package com.infinitepower.newquiz.settings_presentation

sealed interface SettingsScreenUiEvent {
    object DownloadTranslationModel : SettingsScreenUiEvent

    object DeleteTranslationModel : SettingsScreenUiEvent

    object SignOut : SettingsScreenUiEvent

    object ClearWordleCalendarItems : SettingsScreenUiEvent

    data class EnableLoggingAnalytics(
        val enabled: Boolean
    ) : SettingsScreenUiEvent

    object ClearMultiChoiceQuizRecentCategories : SettingsScreenUiEvent
}
