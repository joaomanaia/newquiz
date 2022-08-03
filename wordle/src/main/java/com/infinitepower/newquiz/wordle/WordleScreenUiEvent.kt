package com.infinitepower.newquiz.wordle

sealed class WordleScreenUiEvent {
    data class OnKeyClick(val key: Char) : WordleScreenUiEvent()

    data class OnRemoveKeyClick(val index: Int) : WordleScreenUiEvent()

    object VerifyRow : WordleScreenUiEvent()
}
