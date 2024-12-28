package com.infinitepower.newquiz.feature.maze

import androidx.annotation.Keep
import com.infinitepower.newquiz.feature.maze.common.MazeCategories
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.GameMode
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.maze.emptyMaze

typealias CategoriesByGameMode = Map<GameMode, Set<BaseCategory>>

@Keep
data class MazeScreenUiState(
    val loading: Boolean = true,
    val maze: MazeQuiz = emptyMaze(),
    val autoScrollToCurrentItem: Boolean = true,
    val comparisonQuizCategories: List<BaseCategory> = emptyList(),
) {
    val isMazeEmpty: Boolean
        get() = maze.items.isEmpty()

    val isMazeCompleted: Boolean
        get() = maze.items.all { it.played }

    val mazeSeed: Int?
        get() = maze.items.firstOrNull()?.mazeSeed

    fun getAvailableCategoriesByGameMode(): CategoriesByGameMode {
        return maze.items.groupBy { it.gameMode }.mapValues { (gameMode, categoryItems) ->
            if (categoryItems.isEmpty()) return@mapValues emptySet()

            val availableCategories = when (gameMode) {
                GameMode.MULTI_CHOICE -> MazeCategories.getMultiChoiceCategories()
                GameMode.WORDLE -> MazeCategories.availableWordleCategories
                GameMode.COMPARISON_QUIZ -> comparisonQuizCategories
            }

            categoryItems.mapNotNull { item ->
                availableCategories.find { it.id == item.categoryId }
            }.toSet()
        }
    }

    fun getInvalidMazeItems(availableCategories: CategoriesByGameMode): List<MazeQuiz.MazeItem> {
        return maze.items.filter { item ->
            val categoriesForGameMode = availableCategories[item.gameMode] ?: emptyList()
            !categoriesForGameMode.any { it.id == item.categoryId }
        }
    }
}
