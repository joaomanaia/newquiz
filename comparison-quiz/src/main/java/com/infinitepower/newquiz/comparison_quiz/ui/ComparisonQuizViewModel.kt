package com.infinitepower.newquiz.comparison_quiz.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.data.worker.UpdateGlobalEventDataWorker
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategoryEntity
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComparisonQuizViewModel @Inject constructor(
    private val comparisonQuizCore: ComparisonQuizCore,
    private val savedStateHandle: SavedStateHandle,
    private val comparisonQuizRepository: ComparisonQuizRepository,
    private val workManager: WorkManager,
    private val authUserRepository: AuthUserRepository,
    private val userRepository: UserRepository,
    private val recentCategoriesRepository: RecentCategoriesRepository,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComparisonQuizUiState())
    val uiState = _uiState.asStateFlow()

    init {
        comparisonQuizCore
            .quizDataFlow
            .onEach { data ->
                _uiState.update { currentState ->
                    if (data.currentPosition > currentState.highestPosition) {
                        comparisonQuizRepository.saveHighestPosition(data.currentPosition)
                    }

                    if (data.isGameOver) {
                        viewModelScope.launch(Dispatchers.IO) {
                            UpdateGlobalEventDataWorker.enqueueWork(
                                workManager = workManager,
                                GameEvent.ComparisonQuiz.PlayAndGetScore(data.currentPosition)
                            )

                            analyticsHelper.logEvent(
                                AnalyticsEvent.ComparisonQuizGameEnd(
                                    category = currentState.gameCategory?.id,
                                    comparisonMode = currentState.comparisonMode?.name,
                                    score = data.currentPosition
                                )
                            )
                        }
                    }

                    currentState.copy(
                        currentQuestion = data.currentQuestion,
                        gameDescription = data.questionDescription,
                        currentPosition = data.currentPosition,
                        isGameOver = data.isGameOver
                    )
                }
            }.launchIn(viewModelScope)

        comparisonQuizRepository
            .getHighestPosition()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(highestPosition = res.data ?: 0)
                }
            }.launchIn(viewModelScope)

        // Start game
        viewModelScope.launch(Dispatchers.IO) {
            val category = getCategory()
            val comparisonMode = getComparisonMode()

            // Update initial state with data that don't change during the game.
            _uiState.update { currentState ->
                currentState.copy(
                    gameCategory = category,
                    comparisonMode = comparisonMode,
                    skipCost = comparisonQuizCore.skipCost,
                    isSignedIn = authUserRepository.isSignedIn
                )
            }

            comparisonQuizCore.initializeGame(
                initializationData = ComparisonQuizCore.InitializationData(
                    category = category,
                    comparisonMode = comparisonMode
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
    }

    fun onEvent(event: ComparisonQuizUiEvent) {
        when (event) {
            is ComparisonQuizUiEvent.OnAnswerClick -> {
                comparisonQuizCore.onAnswerClicked(event.item)
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
                    comparisonQuizCore.skip()
                }
            }
        }
    }

    private fun getCategory(): ComparisonQuizCategory {
        val categoryEntity = savedStateHandle
            .get<ComparisonQuizCategoryEntity>(ComparisonQuizListScreenNavArg::category.name)
            ?: throw IllegalArgumentException("Category is null")

        return categoryEntity.toModel()
    }

    private fun getComparisonMode(): ComparisonMode {
        return savedStateHandle
            .get<ComparisonMode>(ComparisonQuizListScreenNavArg::comparisonMode.name)
            ?: throw IllegalArgumentException("Comparison mode is null")
    }

    private fun getUserDiamonds() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { currentState ->
            currentState.copy(userDiamondsLoading = true)
        }

        val user = try {
            userRepository.getLocalUser() ?: throw NullPointerException()
        } catch (e: Exception) {
            e.printStackTrace()
            return@launch
        }

        _uiState.update { currentState ->
            currentState.copy(
                userDiamonds = user.data.diamonds,
                userDiamondsLoading = false
            )
        }
    }
}
