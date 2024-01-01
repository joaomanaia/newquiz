package com.infinitepower.newquiz.feature.maze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.datastore.common.SettingsCommon
import com.infinitepower.newquiz.core.datastore.di.SettingsDataStoreManager
import com.infinitepower.newquiz.core.datastore.manager.DataStoreManager
import com.infinitepower.newquiz.data.worker.maze.CleanMazeQuizWorker
import com.infinitepower.newquiz.data.worker.maze.GenerateMazeQuizWorker
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MazeScreenViewModel @Inject constructor(
    mazeMathQuizRepository: MazeQuizRepository,
    private val workManager: WorkManager,
    private val analyticsHelper: AnalyticsHelper,
    @SettingsDataStoreManager private val settingsDataStoreManager: DataStoreManager
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
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MazeScreenUiState()
    )

    init {
        viewModelScope.launch {
            val autoScrollToCurrentItem = settingsDataStoreManager.getPreference(SettingsCommon.MazeAutoScrollToCurrentItem)

            _uiState.update { currentState ->
                currentState.copy(autoScrollToCurrentItem = autoScrollToCurrentItem)
            }
        }
    }

    fun onEvent(event: MazeScreenUiEvent) {
        when (event) {
            is MazeScreenUiEvent.GenerateMaze -> generateMaze(event.seed, event.selectedMultiChoiceCategories, event.selectedWordleCategories)
            is MazeScreenUiEvent.RestartMaze -> cleanSavedMaze()
        }
    }

    private fun generateMaze(
        seed: Int?,
        selectedMultiChoiceCategories: List<MultiChoiceCategory>,
        selectedWordleCategories: List<WordleCategory>
    ) {
        val workId = GenerateMazeQuizWorker.enqueue(
            workManager = workManager,
            seed = seed,
            multiChoiceCategories = selectedMultiChoiceCategories,
            wordleCategories = selectedWordleCategories
        )

        workManager
            .getWorkInfoByIdLiveData(workId)
            .asFlow()
            .onEach { workInfo ->
                _uiState.update { currentState ->
                    val loading = when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> false
                        else -> true
                    }

                    currentState.copy(loading = loading)
                }
            }.launchIn(viewModelScope)
    }

    private fun cleanSavedMaze() {
        analyticsHelper.logEvent(AnalyticsEvent.RestartMaze)

        CleanMazeQuizWorker.enqueue(workManager)
    }
}
