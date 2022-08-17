package com.infinitepower.newquiz.model.question

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class SelectedAnswer private constructor(val index: Int) {
    companion object {
        val NONE = SelectedAnswer(-1)

        fun fromIndex(index: Int): SelectedAnswer = SelectedAnswer(index)
    }

    private val isNone: Boolean
        get() = index == -1

    val isSelected: Boolean
        get() = !isNone

    fun isCorrect(question: Question): Boolean =
        !isNone && question.correctAns == index

    init {
        require(index >= -1) { "SelectedAnswer index must be greater than -1" }
    }
}