package com.infinitepower.newquiz.feature.settings.screens.translation

interface TranslationScreenUiEvent {
    data object DownloadTranslationModel : TranslationScreenUiEvent

    data object DeleteTranslationModel : TranslationScreenUiEvent
}