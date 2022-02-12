package com.infinitepower.newquiz.compose.core.word_scrambler

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordScrambler @Inject constructor() {
    fun scrambleWord(text: String): String {
        val tempWord = text.toCharArray()
        tempWord.shuffle()
        return String(tempWord)
    }
}