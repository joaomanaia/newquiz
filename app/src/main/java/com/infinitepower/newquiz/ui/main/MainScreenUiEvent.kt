package com.infinitepower.newquiz.ui.main

import androidx.annotation.Keep

sealed interface MainScreenUiEvent {
    @Keep
    data class OnDataAnalyticsConsentClick(
        val agreed: Boolean
    ) : MainScreenUiEvent
}
