package com.infinitepower.newquiz.comparison_quiz.core

import android.util.Log
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core.game.ComparisonQuizCore.InitializationData
import com.infinitepower.newquiz.core.game.ComparisonQuizCore.QuizData
import com.infinitepower.newquiz.core.game.GameOverException
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.core.user_services.InsufficientDiamondsException
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.data.util.mappers.comparisonquiz.toModel
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItemEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val TAG = "ComparisonQuizCoreImpl"

/**
 * Represents the implementation of the [ComparisonQuizCore] interface.
 *
 * @param comparisonQuizRepository The repository for retrieving comparison quiz data.
 */
class ComparisonQuizCoreImpl @Inject constructor(
    private val comparisonQuizRepository: ComparisonQuizRepository,
    private val remoteConfig: RemoteConfig,
    private val analyticsHelper: AnalyticsHelper,
    private val userService: UserService
) : ComparisonQuizCore {
    private val _quizData = MutableStateFlow(QuizData())
    override val quizDataFlow = _quizData.asStateFlow()

    override suspend fun initializeGame(initializationData: InitializationData) {
        val category = initializationData.category
        val questions = initializationData.initialItems
            .map(ComparisonQuizItemEntity::toModel)
            .ifEmpty {
                // If there are no initial items, get the questions from the repository
                comparisonQuizRepository.getQuestions(initializationData.category)
            }

        if (questions.isEmpty()) {
            Log.w(TAG, "No questions found for category ${initializationData.category.id}")
            return endGame()
        }

        val comparisonMode = initializationData.comparisonMode
        val questionDescription = category.getQuestionDescription(comparisonMode)

        val firstItemHelperValue =
            remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_FIRST_ITEM_HELPER_VALUE)

        val quizData = QuizData(
            category = category,
            questions = questions,
            comparisonMode = comparisonMode,
            questionDescription = questionDescription,
            firstItemHelperValueState = firstItemHelperValue,
        )

        analyticsHelper.logEvent(
            AnalyticsEvent.ComparisonQuizGameStart(
                category = category.id,
                comparisonMode = comparisonMode.name,
            )
        )

        _quizData.emit(quizData)
        startGame()
    }

    override fun startGame() {
        _quizData.update { currentData ->
            try {
                currentData.getNextQuestion()
            } catch (e: GameOverException) {
                e.printStackTrace()
                return endGame()
            }
        }
    }

    override fun onAnswerClicked(answer: ComparisonQuizItem) {
        Log.d(TAG, "Answer clicked: $answer")

        _quizData.update { currentData ->
            val currentQuestion = currentData.currentQuestion
            val isQuestionCorrect = currentQuestion != null && currentQuestion.isCorrectAnswer(answer)
            Log.d(TAG, "Is question correct: $isQuestionCorrect")

            // If the current question is null or the answer is correct, get the next question
            if (currentQuestion == null || isQuestionCorrect) {
                try {
                    currentData.getNextQuestion()
                } catch (e: GameOverException) {
                    e.printStackTrace()
                    currentData.copy(
                        currentQuestion = null,
                        isGameOver = true,
                        isLastQuestionCorrect = isQuestionCorrect
                    )
                }
            } else {
                currentData.copy(
                    currentQuestion = null,
                    isGameOver = true,
                    isLastQuestionCorrect = false
                )
            }
        }
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
        get() = remoteConfig.get(RemoteConfigValue.COMPARISON_QUIZ_SKIP_COST).toUInt()

    override suspend fun getUserDiamonds(): UInt = userService.getUserDiamonds()

    override suspend fun skip() {
        Log.d(TAG, "Skipping question")

        val userDiamonds = getUserDiamonds()
        val skipCost = skipCost

        // Check if the user has enough diamonds to skip the question
        if (userDiamonds < skipCost) {
            throw InsufficientDiamondsException(
                diamondsNeeded = skipCost,
                diamondsAvailable = userDiamonds
            )
        }

        // Update the user's diamond count
        userService.addRemoveDiamonds(-skipCost.toInt())

        // Update the quiz data to the next question
        _quizData.update { currentData ->
            try {
                currentData.getNextQuestion(skipped = true)
            } catch (e: GameOverException) {
                e.printStackTrace()
                return endGame()
            }
        }
    }
}
