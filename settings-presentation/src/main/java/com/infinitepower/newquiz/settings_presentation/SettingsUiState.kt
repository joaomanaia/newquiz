package com.infinitepower.newquiz.settings_presentation

import androidx.annotation.Keep
import com.infinitepower.newquiz.settings_presentation.data.SettingsScreenPageData
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey
import com.infinitepower.newquiz.translation.TranslatorModelState
import com.infinitepower.newquiz.translation.TranslatorTargetLanguages

@Keep
data class SettingsUiState(
    val screenKey: ScreenKey = SettingsScreenPageData.MainPage.key,
    val translationModelState: TranslatorModelState = TranslatorModelState.None,
    val userIsSignedIn: Boolean = false,
    val translatorAvailable: Boolean = false,
    val translatorTargetLanguages: TranslatorTargetLanguages = emptyMap(),
    val translatorTargetLanguage: String = "",
)