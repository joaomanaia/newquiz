package com.infinitepower.newquiz.data.local.question

import androidx.annotation.StringRes
import com.infinitepower.newquiz.core.R as CoreR

sealed class QuestionDifficulty(
    val id: String,
    @StringRes val nameRes: Int
) {
    companion object {
        fun items() = listOf(Easy, Medium, Hard)

        fun from(id: String): QuestionDifficulty = when (id) {
            Easy.id -> Easy
            Medium.id -> Medium
            Hard.id -> Hard
            else -> throw IllegalArgumentException("Question difficulty not found")
        }
    }

    object Easy : QuestionDifficulty(
        id = "easy",
        nameRes = CoreR.string.easy
    )

    object Medium : QuestionDifficulty(
        id = "medium",
        nameRes = CoreR.string.medium
    )

    object Hard : QuestionDifficulty(
        id = "hard",
        nameRes = CoreR.string.hard
    )
}