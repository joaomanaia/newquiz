package com.infinitepower.newquiz.maze_quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.data.worker.maze.CleanMazeQuizWorker
import com.infinitepower.newquiz.data.worker.maze.GenerateMazeQuizWorker
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.maze.emptyMaze
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MazeScreenViewModel @Inject constructor(
    private val mazeMathQuizRepository: MazeQuizRepository,
    private val workManager: WorkManager,
    private val analyticsHelper: AnalyticsHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(MazeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        mazeMathQuizRepository
            .getSavedMazeQuizFlow()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(
                        mathMaze = res.data ?: emptyMaze(),
                        loading = res is Resource.Loading
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: MazeScreenUiEvent) {
        when (event) {
            is MazeScreenUiEvent.GenerateMaze -> generateMaze(event.seed, event.gamesModeSelected)
            is MazeScreenUiEvent.RestartMaze -> cleanSavedMaze()
        }
    }

    private fun generateMaze(seed: Int?, gameModesSelected: List<Int>?) {
        val cleanSavedMazeRequest = OneTimeWorkRequestBuilder<CleanMazeQuizWorker>().build()

        // Null if is all game modes enabled
        val gameModes: IntArray? = gameModesSelected?.ifEmpty { null }?.toIntArray()

        val generateMazeRequest = OneTimeWorkRequestBuilder<GenerateMazeQuizWorker>()
            .setInputData(
                workDataOf(
                    GenerateMazeQuizWorker.INPUT_SEED to seed,
                    GenerateMazeQuizWorker.INPUT_GAME_MODES to gameModes
                )
            ).build()

        workManager
            .beginWith(cleanSavedMazeRequest)
            .then(generateMazeRequest)
            .enqueue()

        workManager
            .getWorkInfoByIdLiveData(generateMazeRequest.id)
            .asFlow()
            .onEach { workInfo ->
                _uiState.update { currentState ->
                    val loading = workInfo.state == WorkInfo.State.ENQUEUED
                            || workInfo.state == WorkInfo.State.RUNNING

                    currentState.copy(loading = loading)
                }
            }.launchIn(viewModelScope)
    }

    private fun cleanSavedMaze() {
        analyticsHelper.logEvent(AnalyticsEvent.RestartMaze)

        val cleanSavedMazeRequest = OneTimeWorkRequestBuilder<CleanMazeQuizWorker>().build()

        workManager.enqueue(cleanSavedMazeRequest)
    }
}