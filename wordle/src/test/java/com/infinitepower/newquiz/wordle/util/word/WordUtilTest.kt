package com.infinitepower.newquiz.wordle.util.word

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.wordle.WordleChar
import com.infinitepower.newquiz.model.wordle.WordleItem
import org.junit.jupiter.api.Test

internal class WordUtilTest {

    @Test
    fun `verify from word`() {
        val originalWord = "DBCKE"

        val items = listOf(
            WordleItem.fromChar('A'),
            WordleItem.fromChar('B'),
            WordleItem.fromChar('C'),
            WordleItem.fromChar('D'),
            WordleItem.fromChar('E'),
        )

        val verifiedItems = items verifyFromWord originalWord

        val expectedItems = listOf(
            WordleItem.None(WordleChar('A'), true),
            WordleItem.Correct(WordleChar('B')),
            WordleItem.Correct(WordleChar('C')),
            WordleItem.Present(WordleChar('D')),
            WordleItem.Correct(WordleChar('E')),
        )
        assertThat(verifiedItems).containsExactlyElementsIn(expectedItems)
    }

    @Test
    fun `verify from word, present chars must be same size as original word`() {
        val originalWord = "WORDLE"

        val items = listOf(
            WordleItem.fromChar('E'),
            WordleItem.fromChar('E'),
            WordleItem.fromChar('E'),
            WordleItem.fromChar('E'),
            WordleItem.fromChar('E'),
            WordleItem.fromChar('E'),
        )

        val verifiedItems = items verifyFromWord originalWord

        val expectedItems = listOf(
            WordleItem.None(WordleChar('E'), true),
            WordleItem.None(WordleChar('E'), true),
            WordleItem.None(WordleChar('E'), true),
            WordleItem.None(WordleChar('E'), true),
            WordleItem.None(WordleChar('E'), true),
            WordleItem.Correct(WordleChar('E')),
        )
        assertThat(verifiedItems).containsExactlyElementsIn(expectedItems)
    }

    @Test
    fun `get keys disabled test`() {
        val items = listOf(
            WordleItem.None(WordleChar('A')),
            WordleItem.Present(WordleChar('B')),
            WordleItem.Present(WordleChar('C')),
            WordleItem.None(WordleChar('D')),
            WordleItem.Empty,
            WordleItem.Correct(WordleChar('F')),
        )

        val keysDisabled = items.getKeysDisabled()

        val expectedItems = listOf('A', 'D')

        assertThat(keysDisabled).containsExactlyElementsIn(expectedItems)
    }

    @Test
    fun `get keys disabled test, multiple same char`() {
        // Because there are one correct item, we cannot disabled that key
        val items = listOf(
            WordleItem.None(WordleChar('F')),
            WordleItem.Present(WordleChar('F')),
            WordleItem.Correct(WordleChar('F')),
            WordleItem.None(WordleChar('F')),
            WordleItem.Present(WordleChar('F')),
            WordleItem.Present(WordleChar('F')),
        )

        val keysDisabled = items.getKeysDisabled()
        assertThat(keysDisabled).isEmpty()

        val items2 = listOf(
            WordleItem.None(WordleChar('F')),
            WordleItem.None(WordleChar('F')),
            WordleItem.None(WordleChar('F')),
            WordleItem.None(WordleChar('F')),
            WordleItem.None(WordleChar('F')),
        )

        val keysDisabled2 = items2.getKeysDisabled()

        val expectedItems2 = listOf('F')
        assertThat(keysDisabled2).containsExactlyElementsIn(expectedItems2)
    }
}