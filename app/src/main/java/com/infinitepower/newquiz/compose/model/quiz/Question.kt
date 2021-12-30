package com.infinitepower.newquiz.compose.model.quiz

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import java.security.SecureRandom

@Keep
@Serializable
data class Question (
        val id: Int,
        val description: String,
        val imageUrl: String? = null,
        val options: List<String>,
        val lang: String,
        val category: String,
        val correctAns: Int,
        val type: String,
        val difficulty: String
)

fun getBasicQuestion() = Question(
    id = SecureRandom().nextInt(),
    description = "New Social is the best social network?",
    imageUrl = null,
    options = listOf(
        "No",
        "The Best",
        "Yes",
        "The Worst",
    ),
    QuestionLanguage.EN.name,
    category = "",
    correctAns = 1,
    type = "multiple",
    difficulty = "easy"
)