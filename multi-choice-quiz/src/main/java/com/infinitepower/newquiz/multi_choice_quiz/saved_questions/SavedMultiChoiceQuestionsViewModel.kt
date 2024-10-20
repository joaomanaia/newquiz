package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.ui.SnackbarController
import com.infinitepower.newquiz.data.worker.multichoicequiz.DownloadMultiChoiceQuestionsWorker
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.saved.SortSavedQuestionsBy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class SavedMultiChoiceQuestionsViewModel @Inject constructor(
    private val savedQuestionsRepository: SavedMultiChoiceQuestionsRepository,
    private val workManager: WorkManager,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedMultiChoiceQuestionsUiState())
    val uiState = _uiState.asStateFlow()

    private val sortQuestionsBy = MutableStateFlow(SortSavedQuestionsBy.BY_DEFAULT)

    init {
        sortQuestionsBy
            .flatMapLatest { sortBy ->
                savedQuestionsRepository.getFlowQuestions(sortBy)
            }.onEach { questions ->
                _uiState.update { currentState ->
                    currentState.copy(
                        questions = questions,
                        loading = false
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: SavedMultiChoiceQuestionsUiEvent) {
        when (event) {
            is SavedMultiChoiceQuestionsUiEvent.SelectQuestion -> selectQuestion(event.question)
            is SavedMultiChoiceQuestionsUiEvent.SelectAll -> selectAllQuestions()
            is SavedMultiChoiceQuestionsUiEvent.SelectNone -> _uiState.update {
                it.copy(selectedQuestions = emptyList())
            }

            is SavedMultiChoiceQuestionsUiEvent.DeleteAllSelected -> deleteAllSelected()
            is SavedMultiChoiceQuestionsUiEvent.DownloadQuestions -> downloadQuestions()
            is SavedMultiChoiceQuestionsUiEvent.SortQuestions -> {
                viewModelScope.launch(Dispatchers.IO) {
                    sortQuestionsBy.emit(event.sortBy)
                }
            }
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

    private fun downloadQuestions() {
        analyticsHelper.logEvent(AnalyticsEvent.MultiChoiceDownloadQuestions)

        val downloadQuestionsRequest =
            OneTimeWorkRequestBuilder<DownloadMultiChoiceQuestionsWorker>()
                .setConstraints(
                    Constraints(
                        requiredNetworkType = NetworkType.CONNECTED
                    )
                ).build()

        workManager.enqueue(downloadQuestionsRequest)

        workManager
            .getWorkInfoByIdFlow(downloadQuestionsRequest.id)
            .onEach { info ->
                if (info.state == WorkInfo.State.SUCCEEDED) {
                    SnackbarController.sendShortMessage("Downloaded successfully")
                }

                _uiState.update {
                    it.copy(
                        downloadingQuestions = info.state == WorkInfo.State.RUNNING
                                || info.state == WorkInfo.State.ENQUEUED
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun deleteAllSelected() = viewModelScope.launch(Dispatchers.IO) {
        val allSelectedQuestions = uiState.first().selectedQuestions
        savedQuestionsRepository.deleteAllSelected(allSelectedQuestions)
        // Clear the selected questions after deleting them
        _uiState.update {
            it.copy(selectedQuestions = emptyList())
        }
    }
}