package com.infinitepower.newquiz.wordle

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleRowItem

@Keep
data class WordleScreenUiState(
    val loading: Boolean = true,
    val word: String? = null,
    val rowLimit: Int = Int.MAX_VALUE,
    val rows: List<WordleRowItem> = emptyList(),
    val currentRowPosition: Int = -1,
    val keysDisabled: Set<Char> = emptySet(),
    val day: String? = null,
    val isColorBlindEnabled: Boolean = false,
    val isLetterHintEnabled: Boolean = false,
    val isHardModeEnabled: Boolean = false,
    val errorMessage: String? = null,
    val wordleQuizType: WordleQuizType? = null
) {
    companion object {
        const val ALL_LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM"

        const val allNumbers = "1234567890"

        const val mathFormulaKeys = "0123456789+-*/="
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

    val wordleKeys: CharArray
        get() = when (wordleQuizType) {
            WordleQuizType.TEXT -> ALL_LETTERS.toCharArray()
            WordleQuizType.NUMBER -> allNumbers.toList().toCharArray()
            WordleQuizType.MATH_FORMULA -> mathFormulaKeys.toCharArray()
            else -> charArrayOf()
        }
}