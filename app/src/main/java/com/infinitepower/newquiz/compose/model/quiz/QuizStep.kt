package com.infinitepower.newquiz.compose.model.quiz

data class QuizStep(
    val question: Question,
    val correct: Boolean = false,
    val current: Boolean = false,
    val completed: Boolean = false
)