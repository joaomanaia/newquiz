package com.infinitepower.newquiz.core.common

import com.ramcosta.composedestinations.spec.Direction

sealed class UiEvent {
    object PopBackStack : UiEvent()

    data class Navigate(val direction: Direction) : UiEvent()

    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvent()

    object RefreshData : UiEvent()
}