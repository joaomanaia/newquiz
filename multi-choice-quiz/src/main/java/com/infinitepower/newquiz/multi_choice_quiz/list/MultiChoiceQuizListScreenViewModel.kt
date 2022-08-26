package com.infinitepower.newquiz.multi_choice_quiz.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MultiChoiceQuizListScreenViewModel @Inject constructor(
    private val savedQuestionsRepository: SavedMultiChoiceQuestionsRepository,
    private val multiChoiceQuestionsRepository: MultiChoiceQuestionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MultiChoiceQuizListScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedQuestionsRepository
            .getFlowQuestions()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(savedQuestionsSize = res.size)
                }
            }.launchIn(viewModelScope)

        multiChoiceQuestionsRepository
            .getRecentCategories()
            .onEach { recentCategories ->
                _uiState.update { currentState ->
                    currentState.copy(recentCategories = recentCategories)
                }
            }.launchIn(viewModelScope)
    }
}