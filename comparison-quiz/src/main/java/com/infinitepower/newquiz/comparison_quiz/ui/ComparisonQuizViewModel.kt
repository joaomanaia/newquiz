package com.infinitepower.newquiz.comparison_quiz.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.infinitepower.newquiz.comparison_quiz.core.workers.ComparisonQuizEndGameWorker
import com.infinitepower.newquiz.comparison_quiz.navArgs
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core.ui.SnackbarController
import com.infinitepower.newquiz.core.user_services.InsufficientDiamondsException
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.data.worker.UpdateGlobalEventDataWorker
import com.infinitepower.newquiz.domain.repository.UserConfigRepository
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.model.global_event.GameEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ComparisonQuizViewModel"

@HiltViewModel
class ComparisonQuizViewModel @Inject constructor(
    private val comparisonQuizCore: ComparisonQuizCore,
    savedStateHandle: SavedStateHandle,
    comparisonQuizRepository: ComparisonQuizRepository,
    private val workManager: WorkManager,
    private val recentCategoriesRepository: RecentCategoriesRepository,
    private val userService: UserService,
    private val mazeQuizRepository: MazeQuizRepository,
    private val userConfigRepository: UserConfigRepository
) : ViewModel() {
    private val navArgs: ComparisonQuizScreenNavArg = savedStateHandle.navArgs()

    private val _uiState = MutableStateFlow(ComparisonQuizUiState())
    val uiState = combine(
        _uiState,
        comparisonQuizRepository.getHighestPositionFlow(categoryId = navArgs.categoryId),
        comparisonQuizCore.quizDataFlow
    ) { uiState, highestPosition, quizData ->
        val currentPosition = quizData.currentPosition

        // Get the highest position between the current position and the highest position.
        // The highest position is updated when the game is over.
        val currentHighestPosition = maxOf(currentPosition, highestPosition)

        uiState.copy(
            currentQuestion = quizData.currentQuestion,
            gameDescription = quizData.questionDescription,
            currentPosition = currentPosition,
            isGameOver = quizData.isGameOver,
            isLastQuestionCorrect = quizData.isLastQuestionCorrect,
            firstItemHelperValueState = quizData.firstItemHelperValueState,
            highestPosition = currentHighestPosition
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ComparisonQuizUiState()
    )

    init {
        // Start game
        viewModelScope.launch {
            val category = comparisonQuizRepository.getCategoryById(navArgs.categoryId)
            // TODO: Handle if category is removed/dont exist
            requireNotNull(category)
            val comparisonMode = navArgs.comparisonMode

            // Update initial state with data that don't change during the game.
            _uiState.update { currentState ->
                currentState.copy(
                    gameCategory = category,
                    comparisonMode = comparisonMode,
                    skipCost = comparisonQuizCore.skipCost,
                    userAvailable = userService.userAvailable(),
                    regionalPreferences = userConfigRepository.getRegionalPreferences()
                )
            }

            comparisonQuizCore.initializeGame(
                initializationData = ComparisonQuizCore.InitializationData(
                    category = category,
                    comparisonMode = comparisonMode,
                    initialItems = navArgs.initialItems
                )
            )

            launch {
                recentCategoriesRepository.addComparisonCategory(category.id)

                UpdateGlobalEventDataWorker.enqueueWork(
                    workManager = workManager,
                    GameEvent.ComparisonQuiz.PlayWithComparisonMode(comparisonMode),
                    GameEvent.ComparisonQuiz.PlayQuizWithCategory(category.id)
                )
            }
        }

        comparisonQuizCore
            .quizDataFlow
            .distinctUntilChangedBy { it.isGameOver }
            .onEach { quizData ->
                Log.d(TAG, "Current question: ${quizData.currentQuestion}")

                if (quizData.isGameOver) {
                    Log.d(TAG, "Game over, with position ${quizData.currentPosition}")

                    if (navArgs.mazeItemId != null && quizData.isLastQuestionCorrect) {
                        mazeQuizRepository.completeMazeItem(navArgs.mazeItemId)
                    }

                    UpdateGlobalEventDataWorker.enqueueWork(
                        workManager = workManager,
                        GameEvent.ComparisonQuiz.PlayAndGetScore(quizData.currentPosition)
                    )

                    ComparisonQuizEndGameWorker.enqueueWork(
                        workManager = workManager,
                        categoryId = navArgs.categoryId,
                        comparisonMode = navArgs.comparisonMode,
                        endPosition = quizData.currentPosition,
                        skippedAnswers = quizData.skippedAnswers
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: ComparisonQuizUiEvent) {
        when (event) {
            is ComparisonQuizUiEvent.OnAnswerClick -> {
                viewModelScope.launch {
                    comparisonQuizCore.onAnswerClicked(event.item)
                }
            }

            is ComparisonQuizUiEvent.ShowSkipQuestionDialog -> getUserDiamonds()
            is ComparisonQuizUiEvent.DismissSkipQuestionDialog -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        userDiamonds = -1,
                        userDiamondsLoading = false
                    )
                }
            }

            is ComparisonQuizUiEvent.SkipQuestion -> {
                viewModelScope.launch {
                    try {
                        comparisonQuizCore.skip()
                    } catch (e: InsufficientDiamondsException) {
                        e.printStackTrace()
                        SnackbarController.sendShortMessage("Insufficient diamonds")
                    }
                }
            }
        }
    }

    private fun getUserDiamonds() = viewModelScope.launch {
        _uiState.update { currentState ->
            currentState.copy(userDiamondsLoading = true)
        }

        val userDiamonds = userService.getUserDiamonds()

        _uiState.update { currentState ->
            currentState.copy(
                userDiamonds = userDiamonds.toInt(),
                userDiamondsLoading = false
            )
        }
    }
}
