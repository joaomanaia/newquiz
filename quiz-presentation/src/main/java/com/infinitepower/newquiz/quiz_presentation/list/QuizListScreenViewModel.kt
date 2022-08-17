package com.infinitepower.newquiz.quiz_presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.domain.repository.question.saved_questions.SavedQuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class QuizListScreenViewModel @Inject constructor(
    private val savedQuestionsRepository: SavedQuestionsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuizListScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedQuestionsRepository
            .getFlowQuestions()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(savedQuestionsSize = res.size)
                }
            }.launchIn(viewModelScope)
    }
}