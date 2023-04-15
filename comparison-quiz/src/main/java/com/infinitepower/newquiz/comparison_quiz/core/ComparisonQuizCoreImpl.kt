package com.infinitepower.newquiz.comparison_quiz.core

import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core.game.ComparisonQuizInitialData
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizData
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ComparisonQuizCoreImpl @Inject constructor(
    private val comparisonQuizRepository: ComparisonQuizRepository
) : ComparisonQuizCore {
    private val _quizData = MutableStateFlow(ComparisonQuizData())
    override val quizData = _quizData.asStateFlow()

    override suspend fun loadAndStartGame(initialData: ComparisonQuizInitialData) {
        loadInitialData(initialData)
        startGame()
    }

    override suspend fun loadInitialData(initialData: ComparisonQuizInitialData) {
        comparisonQuizRepository.getQuizData(
            category = initialData.category,
            comparisonMode = initialData.comparisonMode
        ).collect { res ->
            when {
                res.isLoading() -> _quizData.emit(ComparisonQuizData())
                res.isSuccess() && res.data != null -> _quizData.emit(res.data!!)
                res.isError() -> _quizData.emit(ComparisonQuizData(isGameOver = true))
            }
        }
    }

    override fun startGame() {
        _quizData.update { currentData ->
            getNextQuestionData(currentData)
        }
    }

    override fun onAnswerClicked(answer: ComparisonQuizItem) {
        _quizData.update { currentData ->
            val currentQuestion = currentData.currentQuestion

            if (currentQuestion == null || currentQuestion.isCorrectAnswer(answer, currentData.comparisonMode)) {
                getNextQuestionData(currentData)
            } else {
                currentData.copy(
                    currentQuestion = null,
                    isGameOver = true
                )
            }
        }
    }

    private fun getNextQuestionData(currentData: ComparisonQuizData) = try {
        currentData.nextQuestion()
    } catch (e: IllegalStateException) {
        currentData.copy(
            currentQuestion = null,
            isGameOver = true
        )
    }

    override fun endGame() {}
}
