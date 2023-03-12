package com.infinitepower.newquiz.comparison_quiz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.game.ComparisonQuizCore
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
    private val comparisonQuizCore: ComparisonQuizCore
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComparisonQuizUiState())
    val uiState = _uiState.asStateFlow()

    init {
        comparisonQuizCore
            .quizData
            .onEach { data ->
                _uiState.update { currentState ->
                    currentState.copy(
                        currentQuestion = data.currentQuestion,
                        gameDescription = data.gameDescription,
                        currentPosition = data.currentPosition
                    )
                }
            }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            comparisonQuizCore.loadAndStartGame()
        }
    }

    fun onEvent(event: ComparisonQuizUiEvent) {
        when (event) {
            is ComparisonQuizUiEvent.OnAnswerClick -> {
                comparisonQuizCore.nextQuestion()
            }
        }
    }
}
