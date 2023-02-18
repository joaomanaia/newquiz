package com.infinitepower.newquiz.model.wordle

import androidx.annotation.Keep

@Keep
data class WordleWord(
    val word: String,
    val textHelper: String? = null
)