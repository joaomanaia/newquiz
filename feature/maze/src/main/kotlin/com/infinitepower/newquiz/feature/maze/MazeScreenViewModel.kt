package com.infinitepower.newquiz.feature.maze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.data.worker.maze.CleanMazeQuizWorker
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.model.BaseCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MazeScreenViewModel @Inject constructor(
    private val mazeMathQuizRepository: MazeQuizRepository,
    private val workManager: WorkManager,
    private val analyticsHelper: AnalyticsHelper,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager,
    private val comparisonQuizRepository: ComparisonQuizRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MazeScreenUiState())
    val uiState = combine(
        _uiState,
        mazeMathQuizRepository.getSavedMazeQuizFlow()
    ) { uiState, savedMazeQuiz ->
        uiState.copy(
            maze = savedMazeQuiz,
            loading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = MazeScreenUiState()
    )

    init {
        viewModelScope.launch {
            val autoScrollToCurrentItem = settingsDataStoreManager.getPreference(
                preferenceEntry = SettingsCommon.MazeAutoScrollToCurrentItem
            )

            _uiState.update { currentState ->
                currentState.copy(
                    autoScrollToCurrentItem = autoScrollToCurrentItem,
                    comparisonQuizCategories = getComparisonQuizCategories()
                )
            }
        }
    }

    fun onEvent(event: MazeScreenUiEvent) {
        when (event) {
            is MazeScreenUiEvent.RestartMaze -> cleanSavedMaze()
            is MazeScreenUiEvent.RemoveInvalidCategories -> viewModelScope.launch {
                removeInvalidQuestions()
            }
        }
    }

    private fun cleanSavedMaze() {
        analyticsHelper.logEvent(AnalyticsEvent.RestartMaze)

        CleanMazeQuizWorker.enqueue(workManager)
    }

    private suspend fun removeInvalidQuestions() {
        val state = uiState.first()
        val availableCategories = state.getAvailableCategoriesByGameMode()
        val invalidItems = state.getInvalidMazeItems(availableCategories)
        mazeMathQuizRepository.removeItems(invalidItems)
    }

    private fun getComparisonQuizCategories(): List<BaseCategory> {
        return comparisonQuizRepository.getCategories().filterNot { it.isMazeDisabled }
    }
}
