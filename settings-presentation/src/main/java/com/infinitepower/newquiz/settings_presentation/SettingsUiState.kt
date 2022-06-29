package com.infinitepower.newquiz.settings_presentation

import androidx.annotation.Keep
import com.infinitepower.newquiz.settings_presentation.data.SettingsScreenPageData
import com.infinitepower.newquiz.settings_presentation.model.ScreenKey

@Keep
data class SettingsUiState(
    val screenKey: ScreenKey = SettingsScreenPageData.MainPage.key
)