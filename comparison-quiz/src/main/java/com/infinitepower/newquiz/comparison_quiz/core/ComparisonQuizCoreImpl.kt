package com.infinitepower.newquiz.comparison_quiz.core

import androidx.core.net.toUri
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizData
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ComparisonQuizCoreImpl @Inject constructor() : ComparisonQuizCore {
    private val _quizData = MutableStateFlow(ComparisonQuizData())
    override val quizData = _quizData.asStateFlow()

    private fun getTestData(): ComparisonQuizData {
        return ComparisonQuizData(
            gameDescription = "Which country has more population?",
            questions = listOf(
                ComparisonQuizItem(
                    title = "Portugal",
                    value = 3245,
                    imgUri = "https://www.eurodicas.com.br/wp-content/uploads/2019/02/tudo-sobre-portugal.jpg".toUri()
                ),
                ComparisonQuizItem(
                    title = "Spain",
                    value = 23445,
                    imgUri = "https://i.natgeofe.com/k/e800ca90-2b5b-4dad-b4d7-b67a48c96c91/spain-madrid.jpg?w=636&h=358".toUri()
                ),
                ComparisonQuizItem(
                    title = "France",
                    value = 4535,
                    imgUri = "https://i.natgeofe.com/k/04665f4a-3f8d-4b62-8ca3-09ce7dfc5a20/france-eiffel-tower_4x3.jpg".toUri()
                ),
                ComparisonQuizItem(
                    title = "Germany",
                    value = 2132,
                    imgUri = "https://cdn.internationalliving.com/wp-content/uploads/2020/02/Germany-1.jpg".toUri()
                ),
            )
        )
    }

    override suspend fun loadAndStartGame() {
        loadInitialData()
        startGame()
    }

    override suspend fun loadInitialData() {
        _quizData.emit(getTestData())
    }

    override fun startGame() {
        nextQuestion()
    }

    override fun nextQuestion() {
        _quizData.update { currentData ->
            currentData.nextQuestion()
        }
    }

    override fun endGame() {
        TODO("Not yet implemented")
    }
}