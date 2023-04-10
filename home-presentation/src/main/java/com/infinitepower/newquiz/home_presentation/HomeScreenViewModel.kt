package com.infinitepower.newquiz.home_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.common.dataStore.SettingsCommon
import com.infinitepower.newquiz.core.dataStore.manager.DataStoreManager
import com.infinitepower.newquiz.core.di.SettingsDataStoreManager
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.user.auth.AuthUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    @SettingsDataStoreManager private val dataStoreManager: DataStoreManager,
    private val authUserRepository: AuthUserRepository,
    private val multiChoiceQuestionsRepository: MultiChoiceQuestionRepository,
    private val comparisonQuizRepository: ComparisonQuizRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        combine(
            dataStoreManager.getPreferenceFlow(SettingsCommon.ShowLoginCard),
            authUserRepository.isSignedInFlow
        ) { settingsShowLoginCard, isLoggedIn -> settingsShowLoginCard && !isLoggedIn
        }.onEach { showLoginCard ->
            _uiState.update { currentState ->
                currentState.copy(showLoginCard = showLoginCard)
            }
        }.launchIn(viewModelScope)

        multiChoiceQuestionsRepository
            .getRecentCategories()
            .onEach { recentCategories ->
                _uiState.update { currentState ->
                    currentState.copy(multiChoiceRecentCategories = recentCategories)
                }
            }.launchIn(viewModelScope)

        _uiState.update { currentState ->
            val categories = comparisonQuizRepository
                .getCategories()
                .shuffled()
                .take(3)

            currentState.copy(comparisonQuizCategories = categories)
        }
    }

    fun onEvent(event: HomeScreenUiEvent) {
        when (event) {
            is HomeScreenUiEvent.DismissLoginCard -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dataStoreManager.editPreference(SettingsCommon.ShowLoginCard.key, false)
                }
            }
        }
    }
}