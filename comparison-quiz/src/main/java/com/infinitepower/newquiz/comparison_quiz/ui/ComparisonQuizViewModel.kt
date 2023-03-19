package com.infinitepower.newquiz.comparison_quiz.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
import com.infinitepower.newquiz.core.game.ComparisonQuizInitialData
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonModeByFirst
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
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
    private val comparisonQuizRepository: ComparisonQuizRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComparisonQuizUiState())
    val uiState = _uiState.asStateFlow()

    init {
        comparisonQuizCore
            .quizData
            .onEach { data ->
                _uiState.update { currentState ->
                    if (data.currentPosition > currentState.highestPosition) {
                        comparisonQuizRepository.saveHighestPosition(data.currentPosition)
                    }

                    currentState.copy(
                        currentQuestion = data.currentQuestion,
                        gameDescription = data.gameDescription,
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
            comparisonQuizCore.loadAndStartGame(
                initialData = ComparisonQuizInitialData(
                    category = getCategory(),
                    comparisonMode = getComparisonMode()
                )
            )
        }
    }

    fun onEvent(event: ComparisonQuizUiEvent) {
        when (event) {
            is ComparisonQuizUiEvent.OnAnswerClick -> {
                comparisonQuizCore.onAnswerClicked(event.item)
            }
        }
    }

    fun getCategory(): ComparisonQuizCategory {
        return savedStateHandle
            .get<ComparisonQuizCategory>(ComparisonQuizListScreenNavArg::category.name)
            ?: throw IllegalArgumentException("Category is null")
    }

    fun getComparisonMode(): ComparisonModeByFirst {
        return savedStateHandle
            .get<ComparisonModeByFirst>(ComparisonQuizListScreenNavArg::comparisonMode.name)
            ?: throw IllegalArgumentException("Comparison mode is null")
    }
}
