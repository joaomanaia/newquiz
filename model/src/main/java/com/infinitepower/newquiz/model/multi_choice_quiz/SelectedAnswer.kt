package com.infinitepower.newquiz.model.multi_choice_quiz

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class SelectedAnswer private constructor(val index: Int) : java.io.Serializable {
    companion object {
        val NONE = SelectedAnswer(-1)

        fun fromIndex(index: Int): SelectedAnswer = SelectedAnswer(index)
    }

    private val isNone: Boolean
        get() = index == -1

    val isSelected: Boolean
        get() = !isNone

    infix fun isCorrect(question: MultiChoiceQuestion): Boolean =
        !isNone && question.correctAns == index

    init {
        require(index >= -1) { "SelectedAnswer index must be greater than -1" }
    }
}
