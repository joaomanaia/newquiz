package com.infinitepower.newquiz.multi_choice_quiz.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MultiChoiceQuizListScreenViewModel @Inject constructor(
    savedQuestionsRepository: SavedMultiChoiceQuestionsRepository,
    networkStatusTracker: NetworkStatusTracker,
    recentCategoriesRepository: RecentCategoriesRepository
) : ViewModel() {
    // Searching if this is the problem with the TooManyRequestsException
    /*
    val uiState: StateFlow<MultiChoiceQuizListScreenUiState> = combine(
        savedQuestionsRepository.getFlowQuestions(),
        networkStatusTracker.isOnline
    ) { savedQuestions, isOnline ->
        MultiChoiceQuizListScreenUiState(
            savedQuestionsSize = savedQuestions.size,
            internetConnectionAvailable = isOnline
        )
    }.catch {e -> // Try to fix ConnectivityManager$TooManyRequestsException
        e.printStackTrace()
        emit(MultiChoiceQuizListScreenUiState())
    }.flatMapLatest { state ->
        // Get the recent categories
        recentCategoriesRepository
            .getMultiChoiceCategories(isInternetAvailable = state.internetConnectionAvailable)
            .map { homeCategories ->
                state.copy(homeCategories = homeCategories)
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MultiChoiceQuizListScreenUiState()
    )

     */

    val uiState: StateFlow<MultiChoiceQuizListScreenUiState> = combine(
        savedQuestionsRepository.getCount(),
        recentCategoriesRepository.getMultiChoiceCategories(
            isInternetAvailable = networkStatusTracker.isCurrentlyConnected()
        ),
        recentCategoriesRepository.getShowCategoryConnectionInfoFlow()
    ) { savedQuestionsCount, recentCategories, showCategoryConnectionInfo ->
        MultiChoiceQuizListScreenUiState(
            savedQuestionsSize = savedQuestionsCount,
            homeCategories = recentCategories,
            internetConnectionAvailable = networkStatusTracker.isCurrentlyConnected(),
            showCategoryConnectionInfo = showCategoryConnectionInfo
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MultiChoiceQuizListScreenUiState()
    )
}