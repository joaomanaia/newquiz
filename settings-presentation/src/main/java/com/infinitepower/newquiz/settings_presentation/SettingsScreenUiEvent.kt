package com.infinitepower.newquiz.settings_presentation

sealed interface SettingsScreenUiEvent {
    object DownloadTranslationModel : SettingsScreenUiEvent

    object DeleteTranslationModel : SettingsScreenUiEvent

    object SignOut : SettingsScreenUiEvent

    data class EnableLoggingAnalytics(val enabled: Boolean) : SettingsScreenUiEvent
    data class EnableGeneralAnalytics(val enabled: Boolean) : SettingsScreenUiEvent
    data class EnableCrashlytics(val enabled: Boolean) : SettingsScreenUiEvent
    data class EnablePerformanceMonitoring(val enabled: Boolean) : SettingsScreenUiEvent

    object ClearMultiChoiceQuizRecentCategories : SettingsScreenUiEvent
}
