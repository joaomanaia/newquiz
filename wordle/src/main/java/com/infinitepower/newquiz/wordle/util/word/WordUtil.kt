package com.infinitepower.newquiz.wordle.util.word

import com.infinitepower.newquiz.model.wordle.WordleItem

/**
 * Verifies the input [WordleItem] list with [originalWord]
 *
 * This function will map all [WordleItem] list and return the corresponded verified item
 * that can be [WordleItem.None], [WordleItem.Present] and [WordleItem.Correct].
 *
 * ### Mapping List
 * If the item is in the word and in the correct spot returns [WordleItem.Correct].
 * If the item is in the word and not in the correct spot returns [WordleItem.Present].
 * If the item is not in the word returns [WordleItem.None].
 *
 *
 * @param originalWord word to verify items
 * @return verified [WordleItem] items
 * @see [WordleItem]
 * @author João Manaia
 * @since 1.0.0
 */
internal infix fun List<WordleItem>.verifyFromWord(originalWord: String): List<WordleItem> {
    val removeChars = originalWord.filterIndexed { index, char ->
        getOrNull(index)?.char?.value != char
    }.toMutableList()

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

/**
 * Returns `true` if all elements in [lastRevealedHints] are present in this list.
 *
 * @param lastRevealedHints the list of elements to check against this list
 * @return true if all elements in [lastRevealedHints] are present in this list
 */
internal infix fun List<WordleItem>.containsAllLastRevealedHints(
    lastRevealedHints: List<WordleItem>
): Boolean = lastRevealedHints.all { item -> // Check if all elements in lastRevealedHints are present in this list
    // Check if there is at least one element in this list that has the same character as `item`
    this.any { it.char == item.char }
}

/**
 * Gets keys to disable with list of [WordleItem].
 *
 * If the item is [WordleItem.None] will disable the item key.
 * If the list contains one item [WordleItem.Correct] or [WordleItem.Present] and one item [WordleItem.None] will
 * not disable the key.
 *
 * @return keys disabled
 * @see [WordleItem]
 * @author João Manaia
 * @since 1.0.0
 */
internal fun List<WordleItem>.getKeysDisabled(): Set<Char> {
    return filter { item ->
        val sameItemCount = count { char ->
            (char is WordleItem.Correct || char is WordleItem.Present) && char.char == item.char
        }
        item is WordleItem.None && sameItemCount == 0
    }.mapNotNull { item ->
        val char = item.char.value
        if (char == ' ') null else char
    }.toSet()
}