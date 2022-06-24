package com.infinitepower.newquiz.compose.ui.saved_questions_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.infinitepower.newquiz.compose.domain.use_case.saved_questions.DeleteSavedQuestionUseCase
import com.infinitepower.newquiz.compose.domain.use_case.saved_questions.GetAllSavedQuestionsUseCase
import com.infinitepower.newquiz.compose.domain.use_case.saved_questions.GetSavedQuestionsPagingUseCase
import com.infinitepower.newquiz.compose.domain.use_case.saved_questions.SaveQuestionUseCase
import com.infinitepower.newquiz.compose.model.question.Question
import com.infinitepower.newquiz.compose.quiz_presentation.QuizType
import com.infinitepower.newquiz.compose.quiz_presentation.destinations.QuizScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class SavedQuestionsListViewModel @Inject constructor(
    private val getSavedQuestionsPagingUseCase: GetSavedQuestionsPagingUseCase,
    private val saveQuestionUseCase: SaveQuestionUseCase,
    private val deleteSavedQuestionUseCase: DeleteSavedQuestionUseCase,
    private val getAllSavedQuestionsUseCase: GetAllSavedQuestionsUseCase
) : ViewModel() {

    private val _uiEvent = Channel<com.infinitepower.newquiz.compose.core.common.UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedQuestion: Question? = null

    private val _selectedQuestions = MutableStateFlow<List<Question>>(emptyList())
    val selectedQuestions = _selectedQuestions.asStateFlow()

    private val sortOrder = MutableStateFlow(SavedQuestionsListSortOrder.BY_DESCRIPTION)

    val savedQuestions = sortOrder.flatMapLatest { order ->
        Pager(
            PagingConfig(
                pageSize = 16,
                enablePlaceholders = false
            )
        ) {
            getSavedQuestionsPagingUseCase(order)
        }.flow.cachedIn(viewModelScope)
    }

    fun onEvent(event: SavedQuestionsListEvent) = viewModelScope.launch(Dispatchers.IO) {
        when (event) {
            is SavedQuestionsListEvent.OnQuestionClick -> {
                changeQuestionSelection(event.question)
            }
            is SavedQuestionsListEvent.OnDeleteQuestionClick -> {
                deletedQuestion = event.question
                deleteSavedQuestionUseCase(event.question)
                sendUiEvent(com.infinitepower.newquiz.compose.core.common.UiEvent.ShowSnackBar(message = "Question Deleted", action = "Undo"))
                sendUiEvent(com.infinitepower.newquiz.compose.core.common.UiEvent.RefreshData)
            }
            is SavedQuestionsListEvent.OnUndoDeleteClick -> {
                deletedQuestion?.let { question ->
                    saveQuestionUseCase(question)
                    sendUiEvent(com.infinitepower.newquiz.compose.core.common.UiEvent.RefreshData)
                    deletedQuestion = null
                }
            }
            is SavedQuestionsListEvent.OnSelectAllClick -> selectAllQuestions()
            is SavedQuestionsListEvent.OnDeleteAllSelectedClick -> {
                val selectedQuestionsArr = selectedQuestions.first().toTypedArray()
                deleteSavedQuestionUseCase(*selectedQuestionsArr)
            }
            is SavedQuestionsListEvent.OnUnselectAllClick -> _selectedQuestions.emit(emptyList())
            is SavedQuestionsListEvent.OnSortOrderChange -> sortOrder.emit(event.order)
            is SavedQuestionsListEvent.OnPlayQuizGame -> playQuizGame()
        }
    }

    private suspend fun selectAllQuestions() {
        getAllSavedQuestionsUseCase().collect { result ->
            if (result is com.infinitepower.newquiz.compose.core.common.Resource.Success) {
                _selectedQuestions.emit(result.data.orEmpty())
            }
        }
    }

    private suspend fun changeQuestionSelection(question: Question) {
        selectedQuestions.first().toMutableList().apply {
            if (contains(question)) remove(question) else add(question)
        }.also {
            _selectedQuestions.emit(it)
        }
    }

    private suspend fun playQuizGame() {
        val questions = selectedQuestions.first()
        val questionsString = Json.encodeToString(questions)
        //val questionsBase64 = Base64Utils.encode(questionsString.toByteArray())

        sendUiEvent(
            com.infinitepower.newquiz.compose.core.common.UiEvent.Navigate(
                direction = QuizScreenDestination(
                    quizType = QuizType.QUICK_QUIZ,
                    //defaultQuestionsString = questionsString
                )
            )
        )
    }

    private suspend fun sendUiEvent(event: com.infinitepower.newquiz.compose.core.common.UiEvent) {
        _uiEvent.send(event)
    }
}