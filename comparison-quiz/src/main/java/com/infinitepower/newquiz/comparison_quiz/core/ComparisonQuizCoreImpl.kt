package com.infinitepower.newquiz.comparison_quiz.core

import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core.game.ComparisonQuizCore.InitializationData
import com.infinitepower.newquiz.core.game.ComparisonQuizCore.QuizData
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Represents the implementation of the [ComparisonQuizCore] interface.
 *
 * @param comparisonQuizRepository The repository for retrieving comparison quiz data.
 */
class ComparisonQuizCoreImpl @Inject constructor(
    private val comparisonQuizRepository: ComparisonQuizRepository
) : ComparisonQuizCore {
    private val _quizData = MutableStateFlow(QuizData())
    override val quizDataFlow = _quizData.asStateFlow()

    override suspend fun initializeGame(initializationData: InitializationData) {
        comparisonQuizRepository.getQuestions(
            category = initializationData.category
        ).collect { res ->
            when {
                res.isLoading() -> _quizData.emit(QuizData())
                res.isSuccess() && res.data != null && res.data?.isNotEmpty() == true -> {
                    val category = initializationData.category
                    val comparisonMode = initializationData.comparisonMode
                    val questionDescription = category.getQuestionDescription(comparisonMode)

                    val quizData = QuizData(
                        questions = res.data.orEmpty(),
                        comparisonMode = comparisonMode,
                        questionDescription = questionDescription
                    )

                    _quizData.emit(quizData)
                }
                res.isError() -> _quizData.emit(QuizData(isGameOver = true))
            }
        }

        startGame()
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

    private fun getNextQuestionData(currentData: QuizData) = try {
        currentData.getNextQuestion()
    } catch (e: IllegalStateException) {
        currentData.copy(
            currentQuestion = null,
            isGameOver = true
        )
    }

    override fun endGame() {}
}
