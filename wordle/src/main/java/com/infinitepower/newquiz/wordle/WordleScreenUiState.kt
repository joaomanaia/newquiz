package com.infinitepower.newquiz.wordle

import android.util.Log
import androidx.annotation.Keep
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleRowItem

@Keep
data class WordleScreenUiState(
    val loading: Boolean = true,
    val word: String? = null,
    val rowLimit: Int = Int.MAX_VALUE,
    val rows: List<WordleRowItem> = emptyList(),
    val currentRowPosition: Int = -1,
    val keysDisabled: List<Char> = emptyList()
) {
    companion object {
        const val ALL_LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM" // QWERTYUIOPASDFGHJKLZXCVBNM
    }

    val currentRowCompleted: Boolean
        get() = rows
            .lastOrNull()
            ?.isRowCompleted == true

    private val currentRowVerified: Boolean
        get() = rows
            .lastOrNull()
            ?.isRowVerified == true

    val isGamedEnded: Boolean
        get() = currentRowPosition + 1 >= rowLimit && currentRowVerified
}