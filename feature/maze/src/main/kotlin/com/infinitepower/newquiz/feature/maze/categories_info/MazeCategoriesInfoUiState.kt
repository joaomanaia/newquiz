package com.infinitepower.newquiz.feature.maze.categories_info

import com.infinitepower.newquiz.model.BaseCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MazeCategoriesInfoUiState(
    val loading: Boolean = true,
    val multiChoiceCategories: ImmutableList<BaseCategory> = persistentListOf(),
    val wordleCategories: ImmutableList<BaseCategory> = persistentListOf(),
    val comparisonQuizCategories: ImmutableList<BaseCategory> = persistentListOf(),
)
