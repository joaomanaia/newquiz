package com.infinitepower.newquiz.wordle

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.wordle.WordleRowItem

@Keep
data class WordleScreenUiState(
    val loading: Boolean = true,
    val word: String? = null,
    val rowLimit: Int = Int.MAX_VALUE,
    val rows: List<WordleRowItem> = emptyList(),
    val currentRowPosition: Int = -1,
    val keysDisabled: List<Char> = emptyList(),
    val day: String? = null,
    val isColorBlindEnabled: Boolean = false,
    val isLetterHintEnabled: Boolean = false,
    val isHardModeEnabled: Boolean = false,
    val errorMessage: String? = null
) {
    companion object {
        const val ALL_LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM"
    }

    val currentRowCompleted: Boolean
        get() = rows
            .lastOrNull()
            ?.isRowCompleted == true

    private val currentRowCorrect: Boolean
        get() = rows
            .lastOrNull { it.isRowVerified }
            ?.isRowCorrect == true

    val isGamedEnded: Boolean
        get() = currentRowPosition + 1 > rowLimit || currentRowCorrect

    val isGameOver: Boolean
        get() = isGamedEnded && !currentRowCorrect
}