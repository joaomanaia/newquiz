package com.infinitepower.newquiz.ui.main

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.theme.AnimationsEnabled
import com.infinitepower.newquiz.model.DataAnalyticsConsentState

/**
 * Represents the state of the main screen.
 *
 * @param loading True if the main screen state is loading. The splash screen will be shown until this is false.
 */
@Keep
data class MainScreenUiState(
    val loading: Boolean = true,
    val dialogConsent: DataAnalyticsConsentState = DataAnalyticsConsentState.NONE,
    val dailyChallengeClaimableCount: Int = 0,
    val animationsEnabled: AnimationsEnabled = AnimationsEnabled()
)

/*
sealed interface MainScreenUiState {
    data object Loading : MainScreenUiState

    @Keep
    data class Success(
        val signedIn: Boolean = false,
        val settingsShowLoginCard: Boolean = false,
        val dialogConsent: DataAnalyticsConsentState = DataAnalyticsConsentState.NONE,
        val dailyChallengeClaimableCount: Int = 0,
        val animationsEnabled: AnimationsEnabled = AnimationsEnabled()
    ) : MainScreenUiState {
        val showLoginCard: Boolean
            get() = settingsShowLoginCard && !signedIn
    }
}

 */
