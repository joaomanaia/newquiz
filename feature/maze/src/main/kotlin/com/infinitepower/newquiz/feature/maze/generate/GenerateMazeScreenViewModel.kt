package com.infinitepower.newquiz.feature.maze.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.infinitepower.newquiz.core.R as CoreR
import com.infinitepower.newquiz.data.local.multi_choice_quiz.category.multiChoiceQuestionCategories
import com.infinitepower.newquiz.data.local.wordle.WordleCategories
import com.infinitepower.newquiz.data.worker.maze.GenerateMazeQuizWorker
import com.infinitepower.newquiz.model.BaseCategory
import com.infinitepower.newquiz.model.GameMode
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private class NotSupportedGameModeException : IllegalStateException("Not supported game mode")

@HiltViewModel
class GenerateMazeScreenViewModel @Inject constructor(
    private val workManager: WorkManager,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GenerateMazeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    multiChoiceCategories = getMultiChoiceCategories(),
                    wordleCategories = availableWordleCategories.toImmutableList(),
                    loading = false
                )
            }
        }
    }

    companion object {
        private val availableMultiChoiceCategoriesIds = listOf(
            MultiChoiceBaseCategory.Logo.id,
            MultiChoiceBaseCategory.Flag.id,
            MultiChoiceBaseCategory.CountryCapitalFlags.id,
            MultiChoiceBaseCategory.GuessMathSolution.id,
        )

        private val availableWordleCategories = WordleCategories.allCategories.filter { category ->
            category.id != WordleQuizType.NUMBER_TRIVIA.name
        }
    }

    fun onEvent(event: GenerateMazeScreenUiEvent) {
        when (event) {
            is GenerateMazeScreenUiEvent.SelectCategory -> selectCategory(event.category)
            is GenerateMazeScreenUiEvent.GenerateMaze -> generateMaze(seed = event.seed)
            is GenerateMazeScreenUiEvent.SelectAllCategories -> selectAllCategories()
            is GenerateMazeScreenUiEvent.SelectOnlyOfflineCategories -> selectOnlyOffline()
            is GenerateMazeScreenUiEvent.SelectCategories -> selectCategories(
                gameMode = event.gameMode,
                selectAll = event.selectAll
            )
        }
    }

    private fun getMultiChoiceCategories(): ImmutableList<MultiChoiceCategory> {
        val allMultiChoiceCategories = multiChoiceQuestionCategories

        val filteredCategories = allMultiChoiceCategories.filter { category ->
            availableMultiChoiceCategoriesIds.contains(category.id)
        }

        // Because with the implementation with OpenTriviaDB, we can't select
        // specific category, so we need to create a special category
        // for this case, that contains all the categories.
        val othersCategory = MultiChoiceCategory(
            id = "others",
            name = UiText.DynamicString("Others"),
            image = CoreR.drawable.general_knowledge,
            requireInternetConnection = true
        )

        return (filteredCategories + othersCategory).toImmutableList()
    }

    /**
     * Select or deselect a category, depending on the current state.
     */
    private fun selectCategory(category: BaseCategory) {
        _uiState.update { currentState ->
            // Get the list of selected categories, depending on the game mode.
            val selectedCategories = when (category.gameMode) {
                GameMode.MULTI_CHOICE -> currentState.selectedMultiChoiceCategories
                GameMode.WORDLE -> currentState.selectedWordleCategories
                else -> throw NotSupportedGameModeException()
            }.toMutableList()

            // If the category is already selected, then deselect it.
            // If the category is not selected, then select it.
            if (selectedCategories.contains(category)) {
                selectedCategories.remove(category)
            } else {
                selectedCategories.add(category)
            }

            // Update the state, depending on the game mode.
            when (category.gameMode) {
                GameMode.MULTI_CHOICE -> currentState.copy(
                    selectedMultiChoiceCategories = selectedCategories.toImmutableList()
                )
                GameMode.WORDLE -> currentState.copy(
                    selectedWordleCategories = selectedCategories.toImmutableList()
                )
                else -> throw NotSupportedGameModeException()
            }
        }
    }

    private fun selectAllCategories() {
        selectMultiChoiceCategories(selectAll = true)
        selectWordleCategories(selectAll = true)
    }

    private fun selectOnlyOffline() {
        _uiState.update { currentState ->
            val multiChoiceOnlyOffline = currentState.multiChoiceCategories.filter { category ->
                !category.requireInternetConnection
            }

            val wordleOnlyOffline = currentState.wordleCategories.filter { category ->
                !category.requireInternetConnection
            }

            currentState.copy(
                selectedMultiChoiceCategories = multiChoiceOnlyOffline.toImmutableList(),
                selectedWordleCategories = wordleOnlyOffline.toImmutableList()
            )
        }
    }

    private fun selectCategories(
        gameMode: GameMode,
        selectAll: Boolean,
    ) {
        when (gameMode) {
            GameMode.MULTI_CHOICE -> selectMultiChoiceCategories(selectAll)
            GameMode.WORDLE -> selectWordleCategories(selectAll)
            else -> throw NotSupportedGameModeException()
        }
    }

    private fun selectMultiChoiceCategories(selectAll: Boolean) {
        _uiState.update { currentState ->
            if (selectAll) {
                currentState.copy(selectedMultiChoiceCategories = currentState.multiChoiceCategories)
            } else {
                currentState.copy(selectedMultiChoiceCategories = persistentListOf())
            }
        }
    }

    private fun selectWordleCategories(selectAll: Boolean) {
        _uiState.update { currentState ->
            if (selectAll) {
                currentState.copy(selectedWordleCategories = currentState.wordleCategories)
            } else {
                currentState.copy(selectedWordleCategories = persistentListOf())
            }
        }
    }

    private fun generateMaze(seed: Int?) {
        viewModelScope.launch {
            val currentState = uiState.first()

            val workId = GenerateMazeQuizWorker.enqueue(
                workManager = workManager,
                seed = seed,
                multiChoiceCategories = currentState.selectedMultiChoiceCategories,
                wordleCategories = currentState.selectedWordleCategories
            )

            workManager
                .getWorkInfoByIdLiveData(workId)
                .asFlow()
                .onEach { workInfo ->
                    _uiState.update { currentState ->
                        val loading = when (workInfo.state) {
                            WorkInfo.State.SUCCEEDED -> false
                            else -> true
                        }

                        currentState.copy(generatingMaze = loading)
                    }
                }.launchIn(viewModelScope)
        }
    }
}
