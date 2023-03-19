package com.infinitepower.newquiz.core.game

import kotlinx.coroutines.flow.StateFlow

/**
 * The game core of all games.
 * It is responsible for loading the initial data and starting the game.
 * @param QuizDataT The type of the quiz data that is used in the implementation of the game.
 */
sealed interface GameCore <QuizDataT, InitialDataT> {
    val quizData: StateFlow<QuizDataT>

    suspend fun loadAndStartGame(initialData: InitialDataT)

    suspend fun loadInitialData(initialData: InitialDataT)

    fun startGame()

    fun endGame()
}
