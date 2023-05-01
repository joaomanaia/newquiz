package com.infinitepower.newquiz.data.util.translation

import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.core.R as CoreR

fun WordleQuizType?.getWordleTitle() = when (this) {
    WordleQuizType.TEXT -> UiText.StringResource(CoreR.string.guess_the_word)
    WordleQuizType.NUMBER -> UiText.StringResource(CoreR.string.guess_the_number)
    WordleQuizType.MATH_FORMULA -> UiText.StringResource(CoreR.string.guess_math_formula)
    WordleQuizType.NUMBER_TRIVIA -> UiText.StringResource(CoreR.string.number_trivia)
    else -> UiText.StringResource(CoreR.string.wordle)
}
