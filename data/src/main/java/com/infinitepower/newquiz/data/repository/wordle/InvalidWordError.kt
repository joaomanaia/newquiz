package com.infinitepower.newquiz.data.repository.wordle

sealed class InvalidWordError : IllegalArgumentException() {
    data object Empty : InvalidWordError()
    data object NotOnlyLetters : InvalidWordError()
    data object NotOnlyDigits : InvalidWordError()
    data object InvalidMathFormula : InvalidWordError()
}
