package com.infinitepower.newquiz.feature.maze.level_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.feature.maze.destinations.LevelResultsScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LevelResultsScreenViewModel @Inject constructor(
    private val mazeQuizRepository: MazeQuizRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val navArgs = LevelResultsScreenDestination.argsFrom(savedStateHandle)

    private val _uiState = MutableStateFlow(LevelResultsScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val item = mazeQuizRepository.getMazeItemById(navArgs.mazeItemId)

            if (item == null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        loading = false,
                        error = "Item not found"
                    )
                }

                return@launch
            }

            val nextAvailableLevel = mazeQuizRepository.getNextAvailableMazeItem()

            _uiState.update { currentState ->
                currentState.copy(
                    loading = false,
                    completed = item.played,
                    currentItem = item,
                    nextAvailableItem = nextAvailableLevel
                )
            }
        }
    }
}
