package com.infinitepower.newquiz.feature.settings.screens.translation

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.translation.TranslatorModelState
import com.infinitepower.newquiz.core.translation.TranslatorTargetLanguages

@Keep
data class TranslationScreenUiState(
    val translationModelState: TranslatorModelState = TranslatorModelState.None,
    val translatorAvailable: Boolean = false,
    val translatorTargetLanguages: TranslatorTargetLanguages = emptyMap(),
    val translatorTargetLanguage: String = "",
)
