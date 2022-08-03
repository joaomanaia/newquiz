package com.infinitepower.newquiz.wordle

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.analytics.logging.wordle.WordleLoggingAnalytics
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.util.collections.indexOfFirstOrNull
import com.infinitepower.newquiz.domain.repository.wordle.word.WordleRepository
import com.infinitepower.newquiz.model.wordle.WordleItem
import com.infinitepower.newquiz.model.wordle.WordleRowItem
import com.infinitepower.newquiz.model.wordle.countByItem
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
    private val wordleLoggingAnalytics: WordleLoggingAnalytics
) : ViewModel() {
    private val _uiState = MutableStateFlow(WordleScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        generateGame()

        viewModelScope.launch(Dispatchers.IO) {
            uiState
                .distinctUntilChangedBy { state -> state.currentRowPosition }
                .collect { state ->
                    if (state.isGamedEnded) endGame()
                }
        }
    }

    fun onEvent(event: WordleScreenUiEvent) {
        when (event) {
            is WordleScreenUiEvent.OnKeyClick -> addKeyToCurrentRow(event.key)
            is WordleScreenUiEvent.OnRemoveKeyClick -> removeKeyFromCurrentRow(event.index)
            is WordleScreenUiEvent.VerifyRow -> verifyRow()
        }
    }

    private fun generateGame() = viewModelScope.launch(Dispatchers.IO) {
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
                        val rows = List(1) {
                            emptyRowItem(size = word.length)
                        }

                        val rowLimit = savedStateHandle.get<Int>(WordleScreenNavArgs::rowLimit.name) ?: Int.MAX_VALUE

                        wordleLoggingAnalytics.logGameStart(word.length, rowLimit)

                        _uiState.update { currentState ->
                            currentState.copy(
                                loading = false,
                                word = word,
                                rowLimit = rowLimit,
                                rows = rows,
                                currentRowPosition = 0
                            )
                        }
                    }
                }
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

            val newRowPosition = currentState.currentRowPosition + 1

            val newRows = currentState
                .rows
                .toMutableList()
                .apply {
                    set(currentState.currentRowPosition, WordleRowItem(verifiedItems))
                    if (newRowPosition < currentState.rowLimit) add(emptyRowItem(currentState.word.length))
                }

            val keysDisabled = verifiedItems.getKeysDisabled()

            wordleLoggingAnalytics.logRowCompleted(
                wordLength = currentState.word.length,
                maxRows = currentState.rowLimit,
                correctItems = verifiedItems.countByItem<WordleItem.Correct>(),
                presentItems = verifiedItems.countByItem<WordleItem.Present>()
            )

            currentState.copy(
                currentRowPosition = newRowPosition,
                rows = newRows,
                keysDisabled = currentState.keysDisabled + keysDisabled
            )
        }
    }

    private fun endGame() = viewModelScope.launch(Dispatchers.IO) {
        val currentState = uiState.first()
        val wordLength = currentState.word?.length ?: return@launch

        val isLastRowCorrect = currentState.rows.lastOrNull()?.isRowCorrect == true

        wordleLoggingAnalytics.logGameEnd(
            wordLength = wordLength,
            maxRows = currentState.rowLimit,
            lastRow = currentState.currentRowPosition,
            lastRowCorrect = isLastRowCorrect
        )
    }
}