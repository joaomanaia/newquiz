package com.infinitepower.newquiz.ui.main

import androidx.annotation.Keep

sealed interface MainScreenUiEvent {
    @Keep
    data class OnAgreeDisagreeClick(
        val agreed: Boolean
    ) : MainScreenUiEvent

    object DismissLoginCard : MainScreenUiEvent
}
