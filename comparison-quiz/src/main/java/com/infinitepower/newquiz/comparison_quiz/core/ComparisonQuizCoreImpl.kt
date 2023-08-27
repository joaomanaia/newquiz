package com.infinitepower.newquiz.comparison_quiz.core

import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core.game.ComparisonQuizCore.InitializationData
import com.infinitepower.newquiz.core.game.ComparisonQuizCore.QuizData
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.config.RemoteConfigApi
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
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
    private val comparisonQuizRepository: ComparisonQuizRepository,
    private val userRepository: UserRepository,
    private val remoteConfigApi: RemoteConfigApi,
    private val analyticsHelper: AnalyticsHelper
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

                    analyticsHelper.logEvent(
                        AnalyticsEvent.ComparisonQuizGameStart(
                            category = category.id,
                            comparisonMode = comparisonMode.name,
                        )
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

            // If the current question is null or the answer is correct, get the next question
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

    override fun endGame() {
        _quizData.update { currentData ->
            currentData.copy(
                currentQuestion = null,
                isGameOver = true
            )
        }
    }

    override val skipCost: UInt
        get() = remoteConfigApi.getLong("comparison_quiz_skip_cost").toUInt()

    override suspend fun getUserSkips(): UInt = userRepository.getLocalUserDiamonds().toUInt()

    override suspend fun skip() {
        // Check if the user has enough diamonds to skip the question
        if (!canSkip()) {
            throw RuntimeException("You don't have enough diamonds to skip this question")
        }

        // Update the user's diamond count
        userRepository.addLocalUserDiamonds(-skipCost.toInt())

        // Update the quiz data to the next question
        _quizData.update(this::getNextQuestionData)
    }
}
