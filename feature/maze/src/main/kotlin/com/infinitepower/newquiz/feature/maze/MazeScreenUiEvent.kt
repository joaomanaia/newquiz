package com.infinitepower.newquiz.feature.maze

sealed interface MazeScreenUiEvent {
    data object RestartMaze : MazeScreenUiEvent

    data object RemoveInvalidCategories : MazeScreenUiEvent
}
