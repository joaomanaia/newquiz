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
import com.infinitepower.newquiz.data.worker.wordle.WordleEndGameWorker
import com.infinitepower.newquiz.domain.repository.wordle.WordleRepository
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleRowItem
import com.infinitepower.newquiz.model.wordle.emptyRowItem
import com.infinitepower.newquiz.wordle.util.word.getKeysDisabled
import com.infinitepower.newquiz.wordle.util.word.verifyFromWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordleScreenViewModel @Inject constructor(
    private val wordleRepository: WordleRepository,
    private val savedStateHandle: SavedStateHandle,
    private val wordleLoggingAnalytics: WordleLoggingAnalytics,
    private val workManager: WorkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(WordleScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
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
            is WordleScreenUiEvent.AddOneRow -> addNewOneRow()
        }
    }

    private fun generateGame() = viewModelScope.launch(Dispatchers.IO) {
        val initialWord = savedStateHandle.get<String?>(WordleScreenNavArgs::word.name)
        val date = savedStateHandle.get<String?>(WordleScreenNavArgs::date.name)

        if (initialWord != null) {
            generateRows(initialWord, date)
            return@launch
        }

        wordleRepository
            .generateRandomWord()
            .collect { res ->
                Log.d("WordleScreenViewModel", "Word: ${res.data}")

                if (res is Resource.Loading) {
                    _uiState.update { currentState ->
                        currentState.copy(loading = true)
                    }
                }

                if (res is Resource.Success) {
                    res.data?.let { word ->
                       generateRows(word)
                    }
                }
            }
    }

    private fun generateRows(word: String, day: String? = null) {
        val rows = List(1) {
            emptyRowItem(size = word.length)
        }

        val rowLimit = savedStateHandle.get<Int>(WordleScreenNavArgs::rowLimit.name) ?: Int.MAX_VALUE

        wordleLoggingAnalytics.logGameStart(word.length, rowLimit, day)

        _uiState.update { currentState ->
            currentState.copy(
                loading = false,
                word = word,
                rowLimit = rowLimit,
                rows = rows,
                currentRowPosition = 0,
                day = day,
                keysDisabled = emptyList()
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
                        val emptyIndex = indexOfFirstOrNull { item -> item is WordleItem.Empty } ?: return

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
            if (!currentState.currentRowCompleted) return

            val currentRow = currentState.rows.lastOrNull() ?: return
            val currentItems = currentRow.items

            val verifiedItems = currentItems verifyFromWord currentState.word
            val isRowCorrect = verifiedItems.all { it is WordleItem.Correct }

            val newRowPosition = currentState.currentRowPosition + 1

            val newRows = currentState
                .rows
                .toMutableList()
                .apply {
                    set(currentState.currentRowPosition, WordleRowItem(verifiedItems))

                    val gameEnd = newRowPosition >= currentState.rowLimit || isRowCorrect
                    if (!gameEnd) add(emptyRowItem(currentState.word.length))
                }

            val keysDisabled = verifiedItems.getKeysDisabled()

            currentState.copy(
                currentRowPosition = newRowPosition,
                rows = newRows,
                keysDisabled = currentState.keysDisabled + keysDisabled
            )
        }
    }

    private fun endGame() {
        val currentState = uiState.value

        val isLastRowCorrect = currentState.rows.lastOrNull()?.isRowCorrect == true

        val workRequest = OneTimeWorkRequestBuilder<WordleEndGameWorker>()
            .setInputData(
                workDataOf(
                    WordleEndGameWorker.INPUT_WORD to currentState.word,
                    WordleEndGameWorker.INPUT_ROW_LIMIT to currentState.rowLimit,
                    WordleEndGameWorker.INPUT_CURRENT_ROW_POSITION to currentState.currentRowPosition,
                    WordleEndGameWorker.INPUT_IS_LAST_ROW_CORRECT to isLastRowCorrect,
                    WordleEndGameWorker.INPUT_DAY to currentState.day,
                )
            ).build()

        workManager.enqueue(workRequest)
    }

    private fun addNewOneRow() {
        _uiState.update { currentState ->
            val currentWord = currentState.word ?: return

            val newRows = currentState
                .rows
                .toMutableList()
                .apply {
                    add(emptyRowItem(currentWord.length))
                }

            currentState.copy(
                rowLimit = currentState.rowLimit + 1,
                rows = newRows
            )
        }
    }
}