package com.infinitepower.newquiz.ui.main

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.theme.AnimationsEnabled

/**
 * Represents the state of the main screen.
 *
 * @param loading True if the main screen state is loading. The splash screen will be shown until this is false.
 */
@Keep
data class MainScreenUiState(
    val loading: Boolean = true,
    val showDataAnalyticsConsentDialog: Boolean = false,
    val dailyChallengeClaimableCount: Int = 0,
    val animationsEnabled: AnimationsEnabled = AnimationsEnabled(),
    val userDiamonds: UInt = 0u
)
