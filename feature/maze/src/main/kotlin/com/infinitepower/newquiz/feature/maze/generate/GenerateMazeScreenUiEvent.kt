package com.infinitepower.newquiz.feature.maze.generate

import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.GameMode

sealed interface GenerateMazeScreenUiEvent {
    data class GenerateMaze(
        val seed: Int?
    ) : GenerateMazeScreenUiEvent

    data object SelectAllCategories : GenerateMazeScreenUiEvent

    data object SelectOnlyOfflineCategories : GenerateMazeScreenUiEvent

    data class SelectCategories(
        val gameMode: GameMode,
        val selectAll: Boolean,
    ) : GenerateMazeScreenUiEvent

    data class SelectCategory(
        val category: BaseCategory
    ) : GenerateMazeScreenUiEvent
}
