package com.infinitepower.newquiz.wordle.util.word

import com.infinitepower.newquiz.model.wordle.WordleItem

internal infix fun List<WordleItem>.verifyFromWord(originalWord: String): List<WordleItem> {
    val removeChars = originalWord.filterIndexed { index, char ->
        getOrNull(index)?.char?.value != char
    }.toMutableList()

    println(removeChars.toString())

    val newList = mapIndexed { index, wordleItem ->
        val char = wordleItem.char

        val charCorrect = originalWord[index] == char.value
        if (charCorrect) return@mapIndexed WordleItem.Correct(char)

        val charPresent = char.value in originalWord && removeChars.remove(wordleItem.char.value)
        if (charPresent) return@mapIndexed WordleItem.Present(char)

        WordleItem.None(char, true)
    }

    return newList
}

internal fun List<WordleItem>.getKeysDisabled(): Set<Char> {
    return filter { item ->
        val sameItemCount = count { (it is WordleItem.Correct || it is WordleItem.Present) && it.char == item.char }
        item is WordleItem.None && sameItemCount == 0
    }.mapNotNull { item ->
        val char = item.char.value
        if (char == ' ') null else char
    }.toSet()
}