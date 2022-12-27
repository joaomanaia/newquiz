package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.MultiChoiceQuizLoggingAnalytics
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedMultiChoiceQuestionsViewModel @Inject constructor(
    private val savedQuestionsRepository: SavedMultiChoiceQuestionsRepository,
    private val questionRepository: MultiChoiceQuestionRepository,
    private val multiChoiceQuizLoggingAnalytics: MultiChoiceQuizLoggingAnalytics
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedMultiChoiceQuestionsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedQuestionsRepository
            .getFlowQuestions()
            .onEach { questions ->
                _uiState.update { currentState ->
                    currentState.copy(questions = questions)
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: SavedMultiChoiceQuestionsUiEvent) {
        when (event) {
            is SavedMultiChoiceQuestionsUiEvent.SelectQuestion -> selectQuestion(event.question)
            is SavedMultiChoiceQuestionsUiEvent.SelectAll -> selectAllQuestions()
            is SavedMultiChoiceQuestionsUiEvent.DeleteAllSelected -> deleteAllSelected()
            is SavedMultiChoiceQuestionsUiEvent.DownloadQuestions -> downloadQuestions()
        }
    }

    private fun selectQuestion(question: MultiChoiceQuestion) {
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
        multiChoiceQuizLoggingAnalytics.logDownloadQuestions()

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