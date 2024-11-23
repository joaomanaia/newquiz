package com.infinitepower.newquiz.feature.maze.categories_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.domain.repository.comparison_quiz.ComparisonQuizRepository
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.feature.maze.common.MazeCategories
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.GameMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MazeCategoriesInfoViewModel @Inject constructor(
    private val mazeMathQuizRepository: MazeQuizRepository,
    private val comparisonQuizRepository: ComparisonQuizRepository
) : ViewModel() {
    private val categoriesByGameMode = mazeMathQuizRepository
        .getSavedMazeQuizFlow()
        .map { quiz ->
            quiz.items
                .groupBy { item -> item.gameMode }
                .mapValues { (gameMode, categoryItems) ->
                    val categoriesIds = categoryItems.map { it.categoryId }.toSet()

                    if (categoriesIds.isEmpty()) return@mapValues emptyList()

                    val categories = when (gameMode) {
                        GameMode.MULTI_CHOICE -> MazeCategories.getMultiChoiceCategories()
                        GameMode.WORDLE -> MazeCategories.availableWordleCategories
                        GameMode.COMPARISON_QUIZ -> getComparisonQuizCategories()
                    }

                    categories.filter { category -> categoriesIds.contains(category.id) }
                }
        }

    val uiState = categoriesByGameMode.map {
        MazeCategoriesInfoUiState(
            multiChoiceCategories = it[GameMode.MULTI_CHOICE].orEmpty().toImmutableList(),
            wordleCategories = it[GameMode.WORDLE].orEmpty().toImmutableList(),
            comparisonQuizCategories = it[GameMode.COMPARISON_QUIZ].orEmpty().toImmutableList(),
            loading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = MazeCategoriesInfoUiState()
    )

    private fun getComparisonQuizCategories(): List<BaseCategory> {
        return comparisonQuizRepository.getCategories().filterNot { it.isMazeDisabled }
    }
}
