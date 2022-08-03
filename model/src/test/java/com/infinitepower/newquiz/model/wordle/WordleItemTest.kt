package com.infinitepower.newquiz.model.wordle

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class WordleItemTest {
    @Test
    fun isCompleted() {
        val empty = WordleItem.Empty
        assertThat(empty.isCompleted).isFalse()

        val none = WordleItem.None(WordleChar('A'))
        assertThat(none.isCompleted).isTrue()

        val present = WordleItem.Present(WordleChar('A'))
        assertThat(present.isCompleted).isTrue()

        val correct = WordleItem.Correct(WordleChar('A'))
        assertThat(correct.isCompleted).isTrue()
    }

    @Test
    fun isVerified() {
        val empty = WordleItem.Empty
        assertThat(empty.isVerified).isFalse()

        val none = WordleItem.None(WordleChar('A'))
        assertThat(none.isVerified).isFalse()

        val none2 = WordleItem.None(WordleChar('A'), true)
        assertThat(none2.isVerified).isTrue()

        val present = WordleItem.Present(WordleChar('A'))
        assertThat(present.isVerified).isTrue()

        val correct = WordleItem.Correct(WordleChar('A'))
        assertThat(correct.isVerified).isTrue()
    }

    @Test
    fun `list wordle item count by type`() {
        val items = listOf(
            WordleItem.Empty,
            WordleItem.None(WordleChar('A')),
            WordleItem.None(WordleChar('A')),
            WordleItem.Present(WordleChar('A')),
            WordleItem.Present(WordleChar('A')),
            WordleItem.Present(WordleChar('A')),
            WordleItem.Correct(WordleChar('A')),
            WordleItem.Correct(WordleChar('A')),
            WordleItem.Correct(WordleChar('A')),
            WordleItem.Correct(WordleChar('A'))
        )

        assertThat(items.countByItem<WordleItem.Empty>()).isEqualTo(1)
        assertThat(items.countByItem<WordleItem.None>()).isEqualTo(2)
        assertThat(items.countByItem<WordleItem.Present>()).isEqualTo(3)
        assertThat(items.countByItem<WordleItem.Correct>()).isEqualTo(4)
    }
}