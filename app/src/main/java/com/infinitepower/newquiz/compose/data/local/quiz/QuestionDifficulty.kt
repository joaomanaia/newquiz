package com.infinitepower.newquiz.compose.data.local.quiz

import kotlinx.serialization.Serializable

@Serializable
sealed class QuestionDifficulty(
    val keyName: String
) {
    companion object {
        fun fromKeyName(keyName: String) = when(keyName) {
            Easy.keyName -> Easy
            Medium.keyName -> Medium
            Hard.keyName -> Hard
            else -> Medium
        }
    }

    object Easy : QuestionDifficulty(keyName = "easy")

    object Medium : QuestionDifficulty(keyName = "medium")

    object Hard : QuestionDifficulty(keyName = "hard")
}
