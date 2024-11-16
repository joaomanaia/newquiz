package com.infinitepower.newquiz.model.comparison_quiz

typealias QuestionPair = Pair<ComparisonQuizItem, ComparisonQuizItem>

/**
 * Represents a question in the comparison quiz.
 * @param questions Pair of questions to compare.
 * @see ComparisonQuizItem
 */
data class ComparisonQuizQuestion(
    val questions: QuestionPair,
    val categoryId: String,
//    val category: ComparisonQuizCategory,
    val comparisonMode: ComparisonMode
) {
    /**
     * Returns true if the [answer] is the correct answer for the [comparisonMode].
     * If the values are equal, the answer is considered correct.
     *
     * @param answer The user selected answer to check.
     * @return True if the [answer] is the correct answer for the [comparisonMode].
     * @see ComparisonMode
     * @see ComparisonQuizItem
     */
    fun isCorrectAnswer(answer: ComparisonQuizItem): Boolean {
        val correctValue = when (comparisonMode) {
            ComparisonMode.GREATER -> maxOf(questions.first.value, questions.second.value)
            ComparisonMode.LESSER -> minOf(questions.first.value, questions.second.value)
        }

        return answer.value == correctValue
    }

    /**
     * Returns a new [ComparisonQuizQuestion] with the second question of the [questions] as the first one and the [newQuestion] as the second one.
     * @param newQuestion New second question.
     * @return New [ComparisonQuizQuestion] with the second question of the [questions] as the first one.
     * @see ComparisonQuizItem
     */
    fun nextQuestion(newQuestion: ComparisonQuizItem): ComparisonQuizQuestion {
        return copy(questions = questions.second to newQuestion)
    }
}
