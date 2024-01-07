package com.infinitepower.newquiz.core.user_services.ui.profile

interface ProfileScreenUiEvent {
    data class OnFilterByTimeRangeClick(val timeRangeIndex: Int) : ProfileScreenUiEvent
}
