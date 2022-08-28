package com.infinitepower.newquiz.data.local.multi_choice_quiz

import androidx.annotation.StringRes
import com.infinitepower.newquiz.core.R as CoreR

sealed class MultiChoiceQuizDifficulty(
    val id: String,
    @StringRes val nameRes: Int
) {
    companion object {
        fun items() = listOf(Easy, Medium, Hard)
    }

    object Easy : MultiChoiceQuizDifficulty(
        id = "easy",
        nameRes = CoreR.string.easy
    )

    object Medium : MultiChoiceQuizDifficulty(
        id = "medium",
        nameRes = CoreR.string.medium
    )

    object Hard : MultiChoiceQuizDifficulty(
        id = "hard",
        nameRes = CoreR.string.hard
    )
}