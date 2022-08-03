package com.infinitepower.newquiz.model.wordle

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class WordleRowItemTest {

    @Test
    fun isRowCompleted() {
        val rowItem1 = WordleRowItem(items = listOf(WordleItem.Empty))
        assertThat(rowItem1.isRowCompleted).isFalse()

        val rowItem2 = WordleRowItem(items = listOf(WordleItem.fromChar('A')))
        assertThat(rowItem2.isRowCompleted).isTrue()

        val rowItem3 = WordleRowItem(
            items = listOf(
                WordleItem.fromChar('A'),
                WordleItem.Present(WordleChar('B')),
                WordleItem.Correct(WordleChar('C'))
            )
        )
        assertThat(rowItem3.isRowCompleted).isTrue()

        val rowItem4 = WordleRowItem(
            items = listOf(
                WordleItem.fromChar('A'),
                WordleItem.Empty,
                WordleItem.Present(WordleChar('B')),
                WordleItem.Correct(WordleChar('C'))
            )
        )
        assertThat(rowItem4.isRowCompleted).isFalse()
    }

    @Test
    fun isRowVerified() {
        val rowItem1 = WordleRowItem(items = listOf(WordleItem.Empty))
        assertThat(rowItem1.isRowVerified).isFalse()

        val rowItem2 = WordleRowItem(items = listOf(WordleItem.fromChar('A')))
        assertThat(rowItem2.isRowVerified).isFalse()

        val rowItem3 = WordleRowItem(
            items = listOf(
                WordleItem.fromChar('A'),
                WordleItem.Present(WordleChar('B')),
                WordleItem.Correct(WordleChar('C'))
            )
        )
        assertThat(rowItem3.isRowVerified).isFalse()

        val rowItem4 = WordleRowItem(
            items = listOf(
                WordleItem.fromChar('A'),
                WordleItem.Empty,
                WordleItem.Present(WordleChar('B')),
                WordleItem.Correct(WordleChar('C'))
            )
        )
        assertThat(rowItem4.isRowVerified).isFalse()

        val rowItem5 = WordleRowItem(
            items = listOf(
                WordleItem.None(WordleChar('A'), true),
                WordleItem.Present(WordleChar('B')),
                WordleItem.Correct(WordleChar('C'))
            )
        )
        assertThat(rowItem5.isRowVerified).isTrue()

        val rowItem6 = WordleRowItem(items = listOf(WordleItem.Present(WordleChar('A'))))
        assertThat(rowItem6.isRowVerified).isTrue()

        val rowItem7 = WordleRowItem(items = listOf(WordleItem.Correct(WordleChar('A'))))
        assertThat(rowItem7.isRowVerified).isTrue()
    }
}