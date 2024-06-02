package com.infinitepower.newquiz.feature.profile

import com.infinitepower.newquiz.core.user_services.DateTimeRangeFormatter

interface ProfileScreenUiEvent {
    data class OnFilterByTimeRangeClick(val timeRange: DateTimeRangeFormatter) : ProfileScreenUiEvent
}
