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
     * Returns true if the [answer] is the correct answer for the [mode].
     * @param answer The user selected answer to check.
     * @param mode Mode of comparison.
     * @return True if the [answer] is the correct answer for the [mode].
     * @see ComparisonModeByFirst
     * @see ComparisonQuizItem
     */
    fun isCorrectAnswer(
        answer: ComparisonQuizItem,
        mode: ComparisonModeByFirst
    ): Boolean {
        val correctValue = when (mode) {
            ComparisonModeByFirst.GREATER -> maxOf(questions.first.value, questions.second.value)
            ComparisonModeByFirst.LESSER -> minOf(questions.first.value, questions.second.value)
        }

        return answer.value == correctValue
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
