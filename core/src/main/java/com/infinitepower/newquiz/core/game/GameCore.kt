package com.infinitepower.newquiz.core.game

import kotlinx.coroutines.flow.StateFlow

/**
 * Represents the core functionality of a game, providing the basic structure and operations.
 *
 * @param QuizData The data type representing the quiz-specific information.
 * @param InitializationData The data type representing the initialization parameters for the game.
 */
sealed interface GameCore <QuizData, InitializationData> {
    /**
     * A [StateFlow] that emits the current quiz data.
     */
    val quizDataFlow: StateFlow<QuizData>

    /**
     * Initializes and starts the game with the given initial data.
     * This method should be called only once.
     *
     * @param initialData The initial data required to set up the game.
     *
     */
    suspend fun initializeGame(initialData: InitializationData)

    /**
     * Starts the game.
     */
    fun startGame()

    /**
     * Ends the game.
     */
    fun endGame()
}
