package com.infinitepower.newquiz.wordle

sealed interface WordleScreenUiEvent {
    data class OnKeyClick(val key: Char) : WordleScreenUiEvent

    data class OnRemoveKeyClick(val index: Int) : WordleScreenUiEvent

    data object VerifyRow : WordleScreenUiEvent

    data object OnPlayAgainClick : WordleScreenUiEvent
}
