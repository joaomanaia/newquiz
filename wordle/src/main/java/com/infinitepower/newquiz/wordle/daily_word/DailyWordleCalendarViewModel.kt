package com.infinitepower.newquiz.wordle.daily_word

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.infinitepower.newquiz.core.analytics.logging.wordle.WordleLoggingAnalytics
import com.infinitepower.newquiz.core.calendar.MonthView
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
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DailyWordleCalendarViewModel @Inject constructor(
    private val dailyWordleRepository: DailyWordleRepository,
    private val wordleLoggingAnalytics: WordleLoggingAnalytics,
    private val monthView: MonthView
) : NavEventViewModel() {
    private val _uiState = MutableStateFlow(DailyWordleCalendarUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: DailyWordleCalendarUiEvent) {
        when (event) {
            is DailyWordleCalendarUiEvent.OnChangePageIndex -> {
                _uiState.update { currentState ->
                    currentState.copy(pageIndex = event.index)
                }
            }

            is DailyWordleCalendarUiEvent.NextMonth -> {
                monthView.nextMonth()
                updateCalendar()
            }

            is DailyWordleCalendarUiEvent.PreviousMonth -> {
                monthView.previousMonth()
                updateCalendar()
            }

            is DailyWordleCalendarUiEvent.OnDateClick -> dateClicked(event.date)
        }
    }

    init {
        updateCalendar()

        uiState
            .distinctUntilChanged { old, new ->
                old.pageIndex == new.pageIndex && old.firstDayDate == new.firstDayDate
            }.filter { state -> state.firstDayDate != null }
            .flatMapLatest { state ->
                dailyWordleRepository.getCalendarItems(
                    wordSize = state.wordSize,
                    month = state.firstDayDate!!.month,
                    year = state.firstDayDate!!.year
                )
            }.onEach { res ->
                if (res is Resource.Success) {
                    _uiState.update { currentState ->
                        currentState.copy(calendarSavedItems = res.data.orEmpty())
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun updateCalendar() {
        val days = monthView.generateCalendarDays()

        _uiState.update { currentState ->
            currentState.copy(calendarDays = days)
        }
    }

    private fun dateClicked(date: LocalDate) = viewModelScope.launch(Dispatchers.IO) {
        val currentState = uiState.first()

        dailyWordleRepository
            .getDailyWord(date, currentState.wordSize)
            .collect { res ->
                if (res is Resource.Success && res.data != null) {
                    val wordleQuiz = WordleScreenDestination(
                        rowLimit = 6,
                        word = res.data,
                        date = date.toString()
                    )

                    wordleLoggingAnalytics.logDailyWordleItemClick(
                        currentState.wordSize,
                        date.toString()
                    )

                    sendNavEventAsync(NavEvent.Navigate(wordleQuiz))
                    return@collect
                }

                if (res is Resource.Error) {
                    Log.e("DailyWordleCalendar", "Error while loading item", Throwable(res.message))
                    sendNavEventAsync(
                        NavEvent.ShowSnackBar(
                            res.message ?: "Error while loading item"
                        )
                    )
                    return@collect
                }
            }
    }
}