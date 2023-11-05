package com.infinitepower.newquiz.model.comparison_quiz

typealias QuestionPair = Pair<ComparisonQuizItem, ComparisonQuizItem>

/**
 * Represents a question in the comparison quiz.
 * @param questions Pair of questions to compare.
 * @see ComparisonQuizItem
 */
@JvmInline
value class ComparisonQuizCurrentQuestion(
    val questions: QuestionPair
) {
    /**
     * Returns true if the [answer] is the correct answer for the [mode].
     * If the values are equal, the answer is considered correct.
     *
     * @param answer The user selected answer to check.
     * @param mode Mode of comparison.
     * @return True if the [answer] is the correct answer for the [mode].
     * @see ComparisonMode
     * @see ComparisonQuizItem
     */
    fun isCorrectAnswer(
        answer: ComparisonQuizItem,
        mode: ComparisonMode
    ): Boolean {
        val correctValue = when (mode) {
            ComparisonMode.GREATER -> maxOf(questions.first.value, questions.second.value)
            ComparisonMode.LESSER -> minOf(questions.first.value, questions.second.value)
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
