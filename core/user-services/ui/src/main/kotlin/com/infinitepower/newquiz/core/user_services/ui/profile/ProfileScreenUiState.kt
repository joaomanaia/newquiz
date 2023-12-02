package com.infinitepower.newquiz.core.user_services.ui.profile

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.user_services.XpEarnedByDays
import com.infinitepower.newquiz.core.user_services.model.User

@Keep
data class ProfileScreenUiState(
    val loading: Boolean = true,
    val user: User? = null,
    val xpEarnedLast7Days: XpEarnedByDays = emptyMap(),
)
