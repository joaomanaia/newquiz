package com.infinitepower.newquiz.model.wordle

@JvmInline
value class WordleRowItem(
    val items: List<WordleItem>
) {
    val isRowCorrect: Boolean
        get() = items.all { item -> item is WordleItem.Correct }

    val isRowCompleted: Boolean
        get() = items.all { item -> item.isCompleted }

    val isRowVerified: Boolean
        get() = items.all { item -> item.isVerified }
}

fun emptyRowItem(size: Int = 6): WordleRowItem = WordleRowItem(
    items = List(size) {
        WordleItem.Empty
    }
)
