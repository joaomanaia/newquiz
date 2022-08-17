package com.infinitepower.newquiz.quiz_presentation.saved_questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infinitepower.newquiz.domain.repository.question.QuestionRepository
import com.infinitepower.newquiz.domain.repository.question.saved_questions.SavedQuestionsRepository
import com.infinitepower.newquiz.model.question.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedQuestionsViewModel @Inject constructor(
    private val savedQuestionsRepository: SavedQuestionsRepository,
    private val questionRepository: QuestionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedQuestionsUiState())
    val uiState = _uiState.asStateFlow()

    /*
    val questions = Pager(
       config = PagingConfig(
           pageSize = 15,
           prefetchDistance = 30,
           enablePlaceholders = false
       )
    ) {
        savedQuestionsRepository.getPagingQuestions()
    }.flow.cachedIn(viewModelScope)

     */

    init {
        savedQuestionsRepository
            .getFlowQuestions()
            .onEach { questions ->
                _uiState.update { currentState ->
                    currentState.copy(questions = questions)
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: SavedQuestionsUiEvent) {
        when (event) {
            is SavedQuestionsUiEvent.SelectQuestion -> selectQuestion(event.question)
            is SavedQuestionsUiEvent.SelectAll -> selectAllQuestions()
            is SavedQuestionsUiEvent.DeleteAllSelected -> deleteAllSelected()
            is SavedQuestionsUiEvent.DownloadQuestions -> downloadQuestions()
        }
    }

    private fun selectQuestion(question: Question) {
        _uiState.update { currentState ->
            val selectedQuestions = if (question in currentState.selectedQuestions) {
                currentState.selectedQuestions - question
            } else {
                currentState.selectedQuestions + question
            }

            currentState.copy(selectedQuestions = selectedQuestions)
        }
    }

    private fun selectAllQuestions() {
        _uiState.update { currentState ->
            val selectedQuestions = if (currentState.selectedQuestions.isEmpty()) {
                currentState.questions
            } else emptyList()

            currentState.copy(selectedQuestions = selectedQuestions)
        }
    }

    private fun downloadQuestions() = viewModelScope.launch(Dispatchers.IO) {
        val allSavedQuestions = savedQuestionsRepository.getQuestions()
        val questions = questionRepository
            .getRandomQuestions(amount = 50)
            .filter { it !in allSavedQuestions }

        savedQuestionsRepository.insertQuestions(questions)
    }

    private fun deleteAllSelected() = viewModelScope.launch(Dispatchers.IO) {
        val allSelectedQuestions = uiState.first().selectedQuestions
        savedQuestionsRepository.deleteAllSelected(allSelectedQuestions)
    }
}