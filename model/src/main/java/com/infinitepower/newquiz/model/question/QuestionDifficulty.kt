package com.infinitepower.newquiz.model.question

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
sealed class QuestionDifficulty(
    val id: String
) : java.io.Serializable {
    companion object {
        fun items() = listOf(Easy, Medium, Hard)

        fun random(
            random: Random = Random
        ) = items().random(random)

        fun from(id: String): QuestionDifficulty = when (id) {
            Easy.id -> Easy
            Medium.id -> Medium
            Hard.id -> Hard
            else -> throw IllegalArgumentException("Question difficulty not found")
        }
    }

    override fun toString(): String = id

    @Serializable
    object Easy : QuestionDifficulty(id = "easy")

    @Serializable
    object Medium : QuestionDifficulty(id = "medium")

    @Serializable
    object Hard : QuestionDifficulty(id = "hard")
}