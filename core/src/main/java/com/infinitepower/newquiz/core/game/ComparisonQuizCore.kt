package com.infinitepower.newquiz.core.game

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCurrentQuestion
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizHelperValueState
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem

/**
 * Represents the core functionality of a comparison quiz.
 */
interface ComparisonQuizCore :
    GameCore<ComparisonQuizCore.QuizData, ComparisonQuizCore.InitializationData>, SkipGame {
    /**
     * Handles the click event on an answer in the comparison quiz.
     *
     * @param answer The selected answer.
     */
    fun onAnswerClicked(answer: ComparisonQuizItem)

    /**
     * Represents the data structure for a comparison quiz used in the [ComparisonQuizCore].
     *
     * @property questions The list of comparison quiz items.
     * @property questionDescription The description of the quiz question.
     * @property currentQuestion The current question in the quiz.
     * @property comparisonMode The comparison mode for the quiz.
     * @property currentPosition The current position in the quiz.
     * @property isGameOver A flag indicating if the game is over.
     * @property firstItemHelperValueState The state of the first item helper value.
     * @property skippedAnswers The number of skipped answers.
     */
    @Keep
    data class QuizData(
        val questions: List<ComparisonQuizItem> = emptyList(),
        val questionDescription: String? = null,
        val currentQuestion: ComparisonQuizCurrentQuestion? = null,
        val comparisonMode: ComparisonMode = ComparisonMode.GREATER,
        val currentPosition: Int = 0,
        val isGameOver: Boolean = false,
        val firstItemHelperValueState: ComparisonQuizHelperValueState = ComparisonQuizHelperValueState.HIDDEN,
        val skippedAnswers: Int = 0
    ) {
        /**
         * Returns the next question and updates the current question.
         *
         * If the current question is null, it means it is a new game and the function will
         * retrieve the first two questions from the questions list and remove them from the list.
         *
         * If the current question is not null, it means it is not a new game and the function will
         * retrieve the first question from the questions list and remove it from the list.
         *
         * @return The next question.
         * @throws GameOverException If the questions list is empty.
         * @throws GameOverException If the questions list has less than two items.
         */
        fun getNextQuestion(skipped: Boolean = false): QuizData {
            if (questions.isEmpty()) {
                throw GameOverException("Questions list is empty")
            }

            val newQuestions = questions.toMutableList()

            // Checks if is new game, if so, gets the two first questions
            // And then removes from the questions list
            val newCurrentQuestion = if (currentQuestion == null) {
                // Check if there is at least two questions
                if (newQuestions.size < 2) {
                    throw GameOverException("Questions list has less than two items")
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

            val isNewGame = currentQuestion == null

            return copy(
                questions = newQuestions,
                currentQuestion = newCurrentQuestion,
                currentPosition = currentPosition + 1,
                // If it is a new game, then it should show the current helper value
                // If it is next question, then it should show the helper value
                firstItemHelperValueState = if (isNewGame) {
                    firstItemHelperValueState
                } else {
                    ComparisonQuizHelperValueState.NORMAL
                },
                // If the question was skipped, then it should increment the skipped questions
                skippedAnswers = if (skipped) {
                    this.skippedAnswers + 1
                } else {
                    this.skippedAnswers
                }
            )
        }
    }

    /**
     * Represents the initial data for the [ComparisonQuizCore].
     *
     * @property category The category of the comparison quiz.
     * @property comparisonMode The comparison mode for the quiz.
     */
    @Keep
    data class InitializationData(
        val category: ComparisonQuizCategory,
        val comparisonMode: ComparisonMode
    )
}
