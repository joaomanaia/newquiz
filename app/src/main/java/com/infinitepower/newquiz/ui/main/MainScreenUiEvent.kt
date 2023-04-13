package com.infinitepower.newquiz.ui.main

interface MainScreenUiEvent {
    data class OnAgreeDisagreeClick(
        val agreed: Boolean
    ) : MainScreenUiEvent
}
