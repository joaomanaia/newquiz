package com.infinitepower.newquiz.compose.ui.quiz

sealed class QuizOption(val id: Int) {
    companion object {
        fun getById(id: Int): QuizOption = when(id) {
            0 -> QuickQuiz
            else -> QuickQuiz
        }
    }

    object QuickQuiz : QuizOption(id = 0)
}
