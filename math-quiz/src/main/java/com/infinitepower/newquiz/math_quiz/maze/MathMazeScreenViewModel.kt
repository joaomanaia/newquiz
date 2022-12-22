package com.infinitepower.newquiz.math_quiz.maze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.data.worker.math_quiz.maze.CleanMathQuizMazeWorker
import com.infinitepower.newquiz.data.worker.math_quiz.maze.GenerateMathQuizWorker
import com.infinitepower.newquiz.domain.repository.math_quiz.maze.MazeMathQuizRepository
import com.infinitepower.newquiz.model.math_quiz.maze.emptyMathMaze
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MathMazeScreenViewModel @Inject constructor(
    private val mazeMathQuizRepository: MazeMathQuizRepository,
    private val workManager: WorkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MathMazeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        mazeMathQuizRepository
            .getSavedMazeQuizFlow()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(
                        mathMaze = res.data ?: emptyMathMaze(),
                        loading = res is Resource.Loading
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun onEvent(event: MathMazeScreenUiEvent) {
        when (event) {
            is MathMazeScreenUiEvent.GenerateMaze -> generateMaze(seed = event.seed)
            is MathMazeScreenUiEvent.RestartMaze -> cleanSavedMaze()
        }
    }

    private fun generateMaze(seed: Int?) {
        val cleanSavedMazeRequest = OneTimeWorkRequestBuilder<CleanMathQuizMazeWorker>().build()

        val generateMazeRequest = OneTimeWorkRequestBuilder<GenerateMathQuizWorker>()
            .setInputData(
                workDataOf(GenerateMathQuizWorker.SEED_INPUT to seed)
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
        val cleanSavedMazeRequest = OneTimeWorkRequestBuilder<CleanMathQuizMazeWorker>().build()

        workManager.enqueue(cleanSavedMazeRequest)
    }
}