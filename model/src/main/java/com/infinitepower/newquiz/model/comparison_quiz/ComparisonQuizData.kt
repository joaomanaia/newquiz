package com.infinitepower.newquiz.model.comparison_quiz

import androidx.annotation.Keep

enum class ComparisonModeByFirst { GREATER, LESSER }

@Keep
data class ComparisonQuizData(
    val questions: List<ComparisonQuizItem> = emptyList(),
    val questionDescription: String? = null,
    val currentQuestion: ComparisonQuizCurrentQuestion? = null,
    val comparisonMode: ComparisonModeByFirst = ComparisonModeByFirst.GREATER,
    val currentPosition: Int = 0,
    val isGameOver: Boolean = false
) {
    /**
     * Returns the next question and updates the current question
     * If the current question is null, it means that is a new game
     * and it will get the first two questions from the questions list
     * and then remove them from the list
     *
     * If the current question is not null, it means that is not a new game
     * and it will get the first question from the questions list
     * and then remove it from the list
     *
     * @param checkHighestPosition Function that will be called to check if the current position is the highest
     * @return The next question
     * @throws IllegalStateException If the questions list is empty
     * @throws IllegalStateException If the questions list has less than two items
     */
    @Throws(IllegalStateException::class)
    fun nextQuestion(
        checkHighestPosition: () -> Unit = {}
    ): ComparisonQuizData {
        if (questions.isEmpty()) {
            throw IllegalStateException("Questions list is empty")
        }

        val newQuestions = questions.toMutableList()

        // Checks if is new game, if so, gets the two first questions
        // And then removes from the questions list
        val newCurrentQuestion = if (currentQuestion == null) {
            // Check if there is at least two questions
            if (newQuestions.size < 2) {
                throw IllegalStateException("Questions list has less than two items")
            }

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
