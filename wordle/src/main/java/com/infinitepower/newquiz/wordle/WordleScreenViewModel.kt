package com.infinitepower.newquiz.wordle

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.ui.SnackbarController
import com.infinitepower.newquiz.core.util.collections.indexOfFirstOrNull
import com.infinitepower.newquiz.data.repository.wordle.InvalidWordError
import com.infinitepower.newquiz.data.worker.UpdateGlobalEventDataWorker
import com.infinitepower.newquiz.domain.repository.maze.MazeQuizRepository
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleRowItem
import com.infinitepower.newquiz.model.wordle.emptyRowItem
import com.infinitepower.newquiz.model.wordle.itemsToString
import com.infinitepower.newquiz.wordle.util.asUiText
import com.infinitepower.newquiz.wordle.util.word.containsAllLastRevealedHints
import com.infinitepower.newquiz.wordle.util.word.getKeysDisabled
import com.infinitepower.newquiz.wordle.util.word.verifyFromWord
import com.infinitepower.newquiz.wordle.util.worker.WordleEndGameWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.infinitepower.newquiz.core.R as CoreR

private const val TAG = "WordleScreenViewModel"

@HiltViewModel
class WordleScreenViewModel @Inject constructor(
    private val wordleRepository: WordleRepository,
    savedStateHandle: SavedStateHandle,
    private val workManager: WorkManager,
    private val analyticsHelper: AnalyticsHelper,
    private val mazeQuizRepository: MazeQuizRepository
) : ViewModel() {
    private val navArgs: WordleScreenNavArgs = savedStateHandle.navArgs()

    private var _uiState = MutableStateFlow(WordleScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val isColorBlindEnabled = wordleRepository.isColorBlindEnabled()
            val isLetterHintEnabled = wordleRepository.isLetterHintEnabled()
            val isHardModeEnabled = wordleRepository.isHardModeEnabled()

            _uiState.update { currentState ->
                currentState.copy(
                    isColorBlindEnabled = isColorBlindEnabled,
                    isLetterHintEnabled = isLetterHintEnabled,
                    isHardModeEnabled = isHardModeEnabled
                )
            }
        }

        generateGame()
    }

//    override fun onCleared() {
//        endGame()
//        super.onCleared()
//    }

    fun onEvent(event: WordleScreenUiEvent) {
        when (event) {
            is WordleScreenUiEvent.OnKeyClick -> addKeyToCurrentRow(event.key)
            is WordleScreenUiEvent.OnRemoveKeyClick -> removeKeyFromCurrentRow(event.index)
            is WordleScreenUiEvent.VerifyRow -> verifyRow()
            is WordleScreenUiEvent.OnPlayAgainClick -> generateGame()
        }
    }

    private fun generateGame() = viewModelScope.launch(Dispatchers.IO) {
        val quizType = navArgs.quizType

        // Checks if saved state has an initial word.
        // If has, generate the rows with the word, if not create a new word.
        if (navArgs.word != null) {
            generateRows(navArgs.word, quizType, navArgs.textHelper)
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

        // Gets the wordle max rows if the row limit from args is null, if not use the args row limit.
        val rowLimit = wordleRepository.getWordleMaxRows(navArgs.rowLimit)

        viewModelScope.launch {
            UpdateGlobalEventDataWorker.enqueueWork(
                workManager = workManager,
                GameEvent.Wordle.PlayWordWithCategory(quizType)
            )

            analyticsHelper.logEvent(
                AnalyticsEvent.WordleGameStart(
                    wordLength = word.length,
                    maxRows = rowLimit,
                    category = quizType.name,
                    mazeItemId = navArgs.mazeItemId?.toIntOrNull()
                )
            )
        }

        _uiState.update { currentState ->
            currentState.copy(
                loading = false,
                word = word,
                rowLimit = rowLimit,
                rows = rows,
                currentRowPosition = 0,
                keysDisabled = emptySet(),
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
        val state = _uiState.updateAndGet { currentState ->
            if (currentState.word == null) return
            if (currentState.wordleQuizType == null) return
            if (!currentState.currentRowCompleted) return

            // Get current row items, if the current row is null stop the verification.
            // Current row is the last row of the list.
            val currentItems = currentState.rows.lastOrNull()?.items ?: return

            wordleRepository.validateWord(
                currentItems.itemsToString(),
                currentState.wordleQuizType
            ).onFailure { exception ->
                viewModelScope.launch {
                    if (exception is InvalidWordError) {
                        SnackbarController.sendShortMessage(exception.asUiText())
                    } else {
                        SnackbarController.sendShortMessage(
                            UiText.StringResource(CoreR.string.error_verifying_word)
                        )
                    }
                }

                return@updateAndGet currentState.copy()
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
                    viewModelScope.launch {
                        SnackbarController.sendShortMessage(
                            UiText.StringResource(CoreR.string.need_to_use_all_hints_error)
                        )
                    }
                    return@updateAndGet currentState.copy()
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
            )
        }

        if (state.isGamedEnded) {
            endGame(state)
        }
    }

    private fun endGame(currentState: WordleScreenUiState) {
        val isLastRowCorrect = currentState.rows.lastOrNull()?.isRowCorrect == true

        val mazeItemId = navArgs.mazeItemId

        if (mazeItemId != null && isLastRowCorrect) {
            viewModelScope.launch {
                mazeQuizRepository.completeMazeItem(mazeItemId.toInt())
            }
        }

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
    }
}
