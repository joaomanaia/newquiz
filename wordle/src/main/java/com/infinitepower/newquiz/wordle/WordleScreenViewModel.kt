package com.infinitepower.newquiz.wordle

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.infinitepower.newquiz.core.analytics.logging.wordle.WordleLoggingAnalytics
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.util.collections.indexOfFirstOrNull
import com.infinitepower.newquiz.data.worker.UpdateGlobalEventDataWorker
import com.infinitepower.newquiz.data.worker.maze.EndGameMazeQuizWorker
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleRowItem
import com.infinitepower.newquiz.model.wordle.emptyRowItem
import com.infinitepower.newquiz.model.wordle.itemsToString
import com.infinitepower.newquiz.wordle.util.word.containsAllLastRevealedHints
import com.infinitepower.newquiz.wordle.util.word.getKeysDisabled
import com.infinitepower.newquiz.wordle.util.word.verifyFromWord
import com.infinitepower.newquiz.wordle.util.worker.WordleEndGameWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WordleScreenViewModel"

@HiltViewModel
class WordleScreenViewModel @Inject constructor(
    private val wordleRepository: WordleRepository,
    private val savedStateHandle: SavedStateHandle,
    private val wordleLoggingAnalytics: WordleLoggingAnalytics,
    private val workManager: WorkManager
) : ViewModel() {
    private var _uiState = MutableStateFlow(WordleScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        wordleRepository
            .isColorBlindEnabled()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(isColorBlindEnabled = res.data == true)
                }
            }.launchIn(viewModelScope)

        wordleRepository
            .isLetterHintEnabled()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(isLetterHintEnabled = res.data == true)
                }
            }.launchIn(viewModelScope)

        wordleRepository
            .isHardModeEnabled()
            .onEach { res ->
                _uiState.update { currentState ->
                    currentState.copy(isHardModeEnabled = res.data == true)
                }
            }.launchIn(viewModelScope)

        generateGame()
    }

    override fun onCleared() {
        endGame()
        super.onCleared()
    }

    fun onEvent(event: WordleScreenUiEvent) {
        when (event) {
            is WordleScreenUiEvent.OnKeyClick -> addKeyToCurrentRow(event.key)
            is WordleScreenUiEvent.OnRemoveKeyClick -> removeKeyFromCurrentRow(event.index)
            is WordleScreenUiEvent.VerifyRow -> verifyRow()
            is WordleScreenUiEvent.OnPlayAgainClick -> generateGame()
        }
    }

    private fun generateGame() = viewModelScope.launch(Dispatchers.IO) {
        val initialWord = savedStateHandle.get<String?>(WordleScreenNavArgs::word.name)
        val textHelper = savedStateHandle.get<String?>(WordleScreenNavArgs::textHelper.name)

        val quizType = savedStateHandle
            .get<WordleQuizType>(WordleScreenNavArgs::quizType.name)
            ?: WordleQuizType.TEXT

        // Checks if saved state has an initial word.
        // If has, generate the rows with the word, if not create a new word.
        if (initialWord != null) {
            generateRows(initialWord, quizType, textHelper)
            return@launch
        }

        wordleRepository
            .generateRandomWord(quizType)
            .collect { res ->
                if (res is Resource.Loading) {
                    _uiState.update { currentState ->
                        currentState.copy(loading = true)
                    }
                }

                if (res is Resource.Success) {
                    res.data?.let { wordleWord ->
                        generateRows(
                            word = wordleWord.word,
                            quizType = quizType,
                            textHelper = wordleWord.textHelper
                        )
                    }
                }
            }
    }

    private suspend fun generateRows(
        word: String,
        quizType: WordleQuizType,
        textHelper: String? = null
    ) {
        Log.d(TAG, "Word: $word")

        val rows = List(1) {
            emptyRowItem(size = word.length)
        }

        val rowLimit =
            wordleRepository.getWordleMaxRows(savedStateHandle[WordleScreenNavArgs::rowLimit.name])

        val mazeItemId = savedStateHandle.get<String?>(WordleScreenNavArgs::mazeItemId.name)

        viewModelScope.launch(Dispatchers.IO) {
            UpdateGlobalEventDataWorker.enqueueWork(
                workManager = workManager,
                GameEvent.Wordle.PlayWordWithCategory(quizType)
            )

            wordleLoggingAnalytics.logGameStart(word.length, rowLimit, quizType.name, mazeItemId?.toIntOrNull())
        }


        _uiState.update { currentState ->
            currentState.copy(
                loading = false,
                word = word,
                rowLimit = rowLimit,
                rows = rows,
                currentRowPosition = 0,
                keysDisabled = emptySet(),
                errorMessage = null,
                wordleQuizType = quizType,
                textHelper = textHelper
            )
        }
    }


    private fun addKeyToCurrentRow(key: Char) {
        _uiState.update { currentState ->
            val newRows = currentState
                .rows
                .toMutableList()
                .apply {
                    val currentRowItem = this[currentState.currentRowPosition]

                    val newRow = currentRowItem.items.toMutableList().apply {
                        val emptyIndex =
                            indexOfFirstOrNull { item -> item is WordleItem.Empty } ?: return

                        set(emptyIndex, WordleItem.fromChar(key))
                    }

                    set(currentState.currentRowPosition, WordleRowItem(newRow))
                }

            currentState.copy(rows = newRows)
        }
    }

    private fun removeKeyFromCurrentRow(index: Int) {
        _uiState.update { currentState ->
            val newRows = currentState
                .rows
                .toMutableList()
                .apply {
                    val currentRowItem = this[currentState.currentRowPosition]

                    val newRow = currentRowItem.items.toMutableList().apply {
                        set(index, WordleItem.Empty)
                    }

                    set(currentState.currentRowPosition, WordleRowItem(newRow))
                }

            currentState.copy(rows = newRows)
        }
    }

    private fun verifyRow() {
        _uiState.update { currentState ->
            if (currentState.word == null) return
            if (currentState.wordleQuizType == null) return
            if (!currentState.currentRowCompleted) return

            // Get current row items, if the current row is null stop the verification.
            // Current row is the last row of the list.
            val currentItems = currentState.rows.lastOrNull()?.items ?: return

            val wordValid = wordleRepository.validateWord(
                currentItems.itemsToString(),
                currentState.wordleQuizType
            )

            if (!wordValid) {
                return@update currentState.copy(
                    errorMessage = "Left formula is not equal to right solution"
                )
            }

            // Verifies items with the word and verifies if the word is correct
            val verifiedItems = currentItems verifyFromWord currentState.word

            if (currentState.isHardModeEnabled) {
                val last2RowItems = currentState
                    .rows
                    .getOrNull(currentState.rows.lastIndex - 1)
                    ?.items
                    .orEmpty()
                    .filter { it is WordleItem.Correct || it is WordleItem.Present }

                val containsAllLastRevealedHints =
                    verifiedItems containsAllLastRevealedHints last2RowItems

                if (!containsAllLastRevealedHints) {
                    return@update currentState.copy(
                        errorMessage = "You need to use all hints from last row!"
                    )
                }
            }

            val isRowCorrect = verifiedItems.all { it is WordleItem.Correct }

            val newRowPosition = currentState.currentRowPosition + 1

            val newRows = currentState
                .rows
                .toMutableList()
                .apply {
                    // Updates the current row with the verified items
                    set(currentState.currentRowPosition, WordleRowItem(verifiedItems))

                    // Checks if is game end.
                    // Is game end if new row position >= row limit and current row is correct.
                    // If is not game end add new empty row.
                    val gameEnd = newRowPosition >= currentState.rowLimit || isRowCorrect
                    if (!gameEnd) add(emptyRowItem(currentState.word.length))
                }

            val keysDisabled = verifiedItems.getKeysDisabled()

            currentState.copy(
                currentRowPosition = newRowPosition,
                rows = newRows,
                keysDisabled = currentState.keysDisabled + keysDisabled,
                errorMessage = null
            )
        }
    }

    private fun endGame() {
        val currentState = uiState.value

        val isLastRowCorrect = currentState.rows.lastOrNull()?.isRowCorrect == true

        val mazeItemId = savedStateHandle.get<String?>(WordleScreenNavArgs::mazeItemId.name)

        val wordleEndGameWorkRequest = OneTimeWorkRequestBuilder<WordleEndGameWorker>()
            .setInputData(
                workDataOf(
                    WordleEndGameWorker.INPUT_WORD to currentState.word,
                    WordleEndGameWorker.INPUT_ROW_LIMIT to currentState.rowLimit,
                    WordleEndGameWorker.INPUT_CURRENT_ROW_POSITION to currentState.currentRowPosition,
                    WordleEndGameWorker.INPUT_IS_LAST_ROW_CORRECT to isLastRowCorrect,
                    WordleEndGameWorker.INPUT_QUIZ_TYPE to currentState.wordleQuizType?.name,
                    WordleEndGameWorker.INPUT_MAZE_TEM_ID to mazeItemId
                )
            ).build()

        workManager.enqueue(wordleEndGameWorkRequest)

        if (mazeItemId != null && isLastRowCorrect) {
            // Runs the end game maze worker if is maze game mode and the question is correct
            val endGameMazeQuizWorkerRequest = OneTimeWorkRequestBuilder<EndGameMazeQuizWorker>()
                .setInputData(workDataOf(EndGameMazeQuizWorker.INPUT_MAZE_ITEM_ID to mazeItemId.toIntOrNull()))
                .build()

            workManager.enqueue(endGameMazeQuizWorkerRequest)
        }
    }
}