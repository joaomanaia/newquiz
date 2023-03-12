package com.infinitepower.newquiz.model.comparison_quiz

typealias QuestionPair = Pair<ComparisonQuizItem, ComparisonQuizItem>

/**
 * Represents a question in the comparison quiz.
 * @param questions Pair of questions to compare.
 * @throws IllegalArgumentException if questions have the same value.
 * @see ComparisonQuizItem
 */
@JvmInline
value class ComparisonQuizCurrentQuestion(
    val questions: QuestionPair
) {
    init {
        require(questions.first.value != questions.second.value) {
            "Questions must have different values"
        }
    }

    /**
     * Checks if the answer is correct.
     * @param mode Mode of comparison, based on the first question of the [questions].
     * @return True if the answer is correct, false otherwise, depending on the [mode].
     * @see ComparisonModeByFirst
     */
    fun isCorrectAnswer(mode: ComparisonModeByFirst): Boolean {
        return when (mode) {
            ComparisonModeByFirst.GREATER -> questions.first.value > questions.second.value
            ComparisonModeByFirst.LESS -> questions.first.value < questions.second.value
        }
    }

    /**
     * Returns a new [ComparisonQuizCurrentQuestion] with the second question of the [questions] as the first one and the [newQuestion] as the second one.
     * @param newQuestion New second question.
     * @return New [ComparisonQuizCurrentQuestion] with the second question of the [questions] as the first one.
     * @see ComparisonQuizItem
     */
    fun nextQuestion(newQuestion: ComparisonQuizItem): ComparisonQuizCurrentQuestion {
        return ComparisonQuizCurrentQuestion(questions.second to newQuestion)
    }
}
