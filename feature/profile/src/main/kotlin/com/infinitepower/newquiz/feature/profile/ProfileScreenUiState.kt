package com.infinitepower.newquiz.feature.profile

import androidx.annotation.Keep
import com.infinitepower.newquiz.core.user_services.DateTimeRangeFormatter
import com.infinitepower.newquiz.core.user_services.model.User
import com.infinitepower.newquiz.model.TimestampWithXP

@Keep
data class ProfileScreenUiState(
    val loading: Boolean = true,
    val user: User? = null,
    val selectedTimeRange: DateTimeRangeFormatter = DateTimeRangeFormatter.Day,
    val xpEarnedList: List<TimestampWithXP> = emptyList(),
)
