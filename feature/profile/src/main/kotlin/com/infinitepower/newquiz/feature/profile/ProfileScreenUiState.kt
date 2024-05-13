package com.infinitepower.newquiz.feature.profile

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.user_services.TimeRange
import com.infinitepower.newquiz.core.user_services.XpEarnedByDateTime
import com.infinitepower.newquiz.core.user_services.model.User

@Keep
data class ProfileScreenUiState(
    val loading: Boolean = true,
    val user: User? = null,
    val xpEarnedLast7Days: XpEarnedByDateTime = emptyMap(),
    val timeRangeIndex: Int = 1 // This Week
) {
    val timeRange: TimeRange
        get() = when (timeRangeIndex) {
            0 -> TimeRange.Today
            else -> TimeRange.ThisWeek
        }
}
