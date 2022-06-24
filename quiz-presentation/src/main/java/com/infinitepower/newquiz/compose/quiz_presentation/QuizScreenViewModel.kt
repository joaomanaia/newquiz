package com.infinitepower.newquiz.compose.quiz_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.compose.model.question.QuestionStep
import com.infinitepower.newquiz.compose.model.question.getBasicQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(QuizScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: QuizScreenUiEvent) {}

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val uiState = QuizScreenUiState(
                questionSteps = listOf(
                    QuestionStep.Completed(
                        question = getBasicQuestion(),
                        correct = true
                    ),
                    QuestionStep.Completed(
                        question = getBasicQuestion(),
                        correct = false
                    ),
                    QuestionStep.Current(question = getBasicQuestion()),
                    QuestionStep.NotCurrent(question = getBasicQuestion()),
                ),
                currentQuestion = getBasicQuestion(),
                selectedAnswer = SelectedAnswer.fromIndex((0..3).random())
            )

            delay(5000)
            _uiState.emit(uiState)
        }
    }
}