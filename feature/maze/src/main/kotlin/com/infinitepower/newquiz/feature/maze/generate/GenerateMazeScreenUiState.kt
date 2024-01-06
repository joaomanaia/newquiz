package com.infinitepower.newquiz.feature.maze.generate

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.BaseCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Keep
data class GenerateMazeScreenUiState(
    val loading: Boolean = true,
    val generatingMaze: Boolean = false,
    val multiChoiceCategories: ImmutableList<BaseCategory> = persistentListOf(),
    val selectedMultiChoiceCategories: ImmutableList<BaseCategory> = persistentListOf(),
    val wordleCategories: ImmutableList<BaseCategory> = persistentListOf(),
    val selectedWordleCategories: ImmutableList<BaseCategory> = persistentListOf(),
)
