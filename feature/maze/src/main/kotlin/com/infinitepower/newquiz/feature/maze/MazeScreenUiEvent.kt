package com.infinitepower.newquiz.feature.maze

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleCategory

sealed interface MazeScreenUiEvent {
    data class GenerateMaze(
        val seed: Int?,
        val selectedMultiChoiceCategories: List<MultiChoiceCategory>,
        val selectedWordleCategories: List<WordleCategory>
    ) : MazeScreenUiEvent

    data object RestartMaze : MazeScreenUiEvent
}