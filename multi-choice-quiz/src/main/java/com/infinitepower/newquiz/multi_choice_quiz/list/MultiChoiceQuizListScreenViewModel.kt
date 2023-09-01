package com.infinitepower.newquiz.multi_choice_quiz.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class MultiChoiceQuizListScreenViewModel @Inject constructor(
    private val savedQuestionsRepository: SavedMultiChoiceQuestionsRepository,
    private val recentCategoriesRepository: RecentCategoriesRepository,
    private val networkStatusTracker: NetworkStatusTracker
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

        networkStatusTracker
            .isOnline
            .onEach { isOnline ->
                _uiState.update { currentState ->
                    currentState.copy(internetConnectionAvailable = isOnline)
                }
            }.flatMapLatest { isOnline ->
                recentCategoriesRepository.getMultiChoiceCategories(isInternetAvailable = isOnline)
            }.onEach { homeCategories ->
                _uiState.update { currentState ->
                    currentState.copy(homeCategories = homeCategories)
                }
            }.launchIn(viewModelScope)
    }
}