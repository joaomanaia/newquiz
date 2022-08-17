package com.infinitepower.newquiz.wordle.daily_word

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.core.common.viewmodel.NavEvent
import com.infinitepower.newquiz.core.common.viewmodel.NavEventViewModel
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DailyWordleSelectorViewModel @Inject constructor(
    private val dailyWordleRepository: DailyWordleRepository
) : NavEventViewModel() {
    private val _uiState = MutableStateFlow(DailyWordleSelectorUiState())
    val uiState = _uiState.asStateFlow()

    private val tz = TimeZone.currentSystemDefault()

    fun onEvent(event: DailyWordleSelectorUiEvent) {
        when (event) {
            is DailyWordleSelectorUiEvent.OnChangePageIndex -> {
                _uiState.update { currentState ->
                    currentState.copy(pageIndex = event.index)
                }
            }
            is DailyWordleSelectorUiEvent.OnChangeInstant -> {
                _uiState.update { currentState ->
                    currentState.copy(instant = event.instant)
                }
            }
            is DailyWordleSelectorUiEvent.OnDateClick -> dateClicked(event.date)
        }
    }

    init {
        uiState
            .distinctUntilChanged { old, new ->
                old.instant == new.instant && old.pageIndex == new.pageIndex
            }.flatMapLatest { state ->
                val localDate = state.instant.toLocalDateTime(tz).date

                dailyWordleRepository.getCalendarItems(
                    wordSize = state.wordSize,
                    month = localDate.month,
                    year = localDate.year
                )
            }.onEach { res ->
                if (res is Resource.Success) {
                    _uiState.update { currentState ->
                        currentState.copy(calendarItems = res.data.orEmpty())
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun dateClicked(date: LocalDate) = viewModelScope.launch(Dispatchers.IO) {
        val currentState = uiState.first()

        dailyWordleRepository
            .getDailyWord(date, currentState.wordSize)
            .collect { res ->
                if (res is Resource.Success && res.data != null) {
                    val wordleQuiz = WordleScreenDestination(rowLimit = 6, word = res.data, date = date.toString())
                    sendNavEventAsync(NavEvent.Navigate(wordleQuiz))
                    return@collect
                }

                if (res is Resource.Error) {
                    Log.e("DailyWordleSelector", "Error while loading item", Throwable(res.message))
                    sendNavEventAsync(NavEvent.ShowSnackBar(res.message ?: "Error while loading item"))
                    return@collect
                }
            }
    }
}