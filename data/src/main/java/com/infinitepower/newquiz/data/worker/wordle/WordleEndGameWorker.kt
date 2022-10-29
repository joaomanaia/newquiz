package com.infinitepower.newquiz.data.worker.wordle

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.logging.wordle.WordleLoggingAnalytics
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.model.wordle.daily.CalendarItemState
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

@HiltWorker
class WordleEndGameWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val wordleLoggingAnalytics: WordleLoggingAnalytics,
    private val dailyWordleRepository: DailyWordleRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val INPUT_WORD = "word"
        const val INPUT_ROW_LIMIT = "row_limit"
        const val INPUT_CURRENT_ROW_POSITION = "current_row_position"
        const val INPUT_IS_LAST_ROW_CORRECT = "is_last_row_correct"
        const val INPUT_DAY = "day"
    }

    override suspend fun doWork(): Result {
        val word = inputData.getString(INPUT_WORD) ?: return Result.failure()
        val rowLimit = inputData.getInt(INPUT_ROW_LIMIT, 0)
        val currentRowPosition = inputData.getInt(INPUT_CURRENT_ROW_POSITION, 0)
        val isLastRowCorrect = inputData.getBoolean(INPUT_IS_LAST_ROW_CORRECT, false)
        val day = inputData.getString(INPUT_DAY)

        wordleLoggingAnalytics.logGameEnd(
            wordLength = word.length,
            maxRows = rowLimit,
            lastRow = currentRowPosition,
            lastRowCorrect = isLastRowCorrect,
            day = day
        )

        if (day != null) {
            saveDailyItemToCalendar(
                day = day.toLocalDate(),
                isCorrect = isLastRowCorrect,
                wordLength = word.length
            )
        }

        return Result.success()
    }

    private suspend fun saveDailyItemToCalendar(
        day: LocalDate,
        isCorrect: Boolean,
        wordLength: Int
    ) {
        val item = WordleDailyCalendarItem(
            date = day,
            state = CalendarItemState.stateByGameWin(isCorrect),
            wordSize = wordLength
        )

        dailyWordleRepository.insertCalendarItem(item)
    }
}