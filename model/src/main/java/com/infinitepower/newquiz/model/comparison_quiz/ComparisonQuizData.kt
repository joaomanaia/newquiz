package com.infinitepower.newquiz.model.comparison_quiz

import androidx.annotation.Keep

enum class ComparisonModeByFirst { GREATER, LESSER }

@Keep
data class ComparisonQuizData(
    val questions: List<ComparisonQuizItem> = emptyList(),
    val gameDescription: String? = null,
    val currentQuestion: ComparisonQuizCurrentQuestion? = null,
    val comparisonMode: ComparisonModeByFirst = ComparisonModeByFirst.GREATER,
    val currentPosition: Int = 0,
    val isGameOver: Boolean = false
) {
    fun nextQuestion(
        checkHighestPosition: () -> Unit = {}
    ): ComparisonQuizData {
        if (questions.isEmpty()) {
            return copy(currentQuestion = null)
        }

        val newQuestions = questions.toMutableList()

        // Checks if is new game, if so, gets the two first questions
        // And then removes from the questions list
        val newCurrentQuestion = if (currentQuestion == null) {
            val firstQuestion = newQuestions.first()
            val secondQuestion = newQuestions[1]

            newQuestions.removeFirst()
            newQuestions.removeAt(0)

            ComparisonQuizCurrentQuestion(firstQuestion to secondQuestion)
        } else {
            // If is not a new game, gets the first question from the questions list
            // And then removes from the questions list
            val firstQuestion = newQuestions.first()
            newQuestions.removeFirst()

            currentQuestion.nextQuestion(firstQuestion)
        }

        checkHighestPosition()

        return copy(
            questions = newQuestions,
            currentQuestion = newCurrentQuestion,
            currentPosition = currentPosition + 1
        )
    }
}
