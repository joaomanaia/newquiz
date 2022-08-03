package com.infinitepower.newquiz.model.wordle

@JvmInline
value class WordleChar(val value: Char) {
    companion object {
        val Empty = WordleChar(' ')
    }

    override fun toString(): String = value.toString()

    fun isEmpty(): Boolean = value == ' '
}

sealed class WordleItem {
    abstract val char: WordleChar

    object Empty : WordleItem() {
        override val char: WordleChar
            get() = WordleChar.Empty
    }

    data class None(
        override val char: WordleChar,
        val verified: Boolean = false
    ) : WordleItem()

    data class Present(
        override val char: WordleChar
    ) : WordleItem()

    data class Correct(
        override val char: WordleChar
    ) : WordleItem()

    val isCompleted: Boolean
        get() = this !is Empty

    val isVerified: Boolean
        get() = when (this) {
            is Empty -> false
            is None -> this.verified
            else -> true
        }

    companion object {
        fun fromChar(char: Char) = None(WordleChar(char), false)
    }
}

inline fun <reified T> List<WordleItem>.countByItem(): Int = filterIsInstance<T>().count()