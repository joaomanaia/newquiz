package com.infinitepower.newquiz.ui.main

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.DataAnalyticsConsentState

@Keep
data class MainScreenUiState(
    val signedIn: Boolean = false,
    val settingsShowLoginCard: Boolean = false,
    val dialogConsent: DataAnalyticsConsentState = DataAnalyticsConsentState.NONE,
    val consentStateLoading: Boolean = true,
    val dailyChallengeClaimableCount: Int = 0,
) {
    val showLoginCard: Boolean
        get() = settingsShowLoginCard && !signedIn
}
