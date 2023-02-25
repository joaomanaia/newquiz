package com.infinitepower.newquiz.model.question

import androidx.annotation.StringRes
import com.infinitepower.newquiz.model.R
import kotlinx.serialization.Serializable

@Serializable
sealed class QuestionDifficulty(
    val id: String,
    @StringRes val nameRes: Int
) : java.io.Serializable {
    companion object {
        fun items() = listOf(Easy, Medium, Hard)

        fun from(id: String): QuestionDifficulty = when (id) {
            Easy.id -> Easy
            Medium.id -> Medium
            Hard.id -> Hard
            else -> throw IllegalArgumentException("Question difficulty not found")
        }
    }

    override fun toString(): String = id

    @Serializable
    object Easy : QuestionDifficulty(
        id = "easy",
        nameRes = R.string.easy
    )

    @Serializable
    object Medium : QuestionDifficulty(
        id = "medium",
        nameRes = R.string.medium
    )

    @Serializable
    object Hard : QuestionDifficulty(
        id = "hard",
        nameRes = R.string.hard
    )
}