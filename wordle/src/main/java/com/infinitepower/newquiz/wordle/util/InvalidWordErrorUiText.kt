package com.infinitepower.newquiz.wordle.util

import com.infinitepower.newquiz.data.repository.wordle.InvalidWordError
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.core.R as CoreR

fun InvalidWordError.asUiText(): UiText {
    return when (this) {
        InvalidWordError.Empty -> UiText.StringResource(CoreR.string.empty_word)
        InvalidWordError.NotOnlyLetters -> UiText.StringResource(CoreR.string.word_not_only_letters_error)
        InvalidWordError.NotOnlyDigits -> UiText.StringResource(CoreR.string.word_not_only_digits_error)
        InvalidWordError.InvalidMathFormula -> UiText.StringResource(CoreR.string.word_invalid_math_formula_error)
    }
}
