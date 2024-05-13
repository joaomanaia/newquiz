package com.infinitepower.newquiz.feature.profile

interface ProfileScreenUiEvent {
    data class OnFilterByTimeRangeClick(val timeRangeIndex: Int) : ProfileScreenUiEvent
}
