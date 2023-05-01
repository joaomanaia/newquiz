package com.infinitepower.newquiz.wordle.util.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.logging.maze.MazeLoggingAnalytics
import com.infinitepower.newquiz.core.analytics.logging.wordle.WordleLoggingAnalytics
import com.infinitepower.newquiz.core.util.kotlin.toLong
import com.infinitepower.newquiz.data.worker.UpdateGlobalEventDataWorker
import com.infinitepower.newquiz.domain.repository.wordle.daily.DailyWordleRepository
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.daily.CalendarItemState
import com.infinitepower.newquiz.model.wordle.daily.WordleDailyCalendarItem
import com.infinitepower.newquiz.online_services.core.OnlineServicesCore
import com.infinitepower.newquiz.online_services.domain.game.xp.WordleXpRepository
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

@HiltWorker
class WordleEndGameWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val dailyWordleRepository: DailyWordleRepository,
    private val onlineServicesCore: OnlineServicesCore,
    private val userRepository: UserRepository,
    private val wordleXpRepository: WordleXpRepository,
    private val wordleLoggingAnalytics: WordleLoggingAnalytics,
    private val mazeLoggingAnalytics: MazeLoggingAnalytics,
    private val workManager: WorkManager
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val INPUT_WORD = "word"
        const val INPUT_ROW_LIMIT = "row_limit"
        const val INPUT_CURRENT_ROW_POSITION = "current_row_position"
        const val INPUT_IS_LAST_ROW_CORRECT = "is_last_row_correct"
        const val INPUT_QUIZ_TYPE = "quiz_type"
        const val INPUT_DAY = "day"
        const val INPUT_MAZE_TEM_ID = "maze_item_id"
    }

    override suspend fun doWork(): Result {
        val word = inputData.getString(INPUT_WORD) ?: return Result.failure()
        val rowLimit = inputData.getInt(INPUT_ROW_LIMIT, 0)
        val currentRowPosition = inputData.getInt(INPUT_CURRENT_ROW_POSITION, 0)
        val isLastRowCorrect = inputData.getBoolean(INPUT_IS_LAST_ROW_CORRECT, false)
        val quizType = inputData.getString(INPUT_QUIZ_TYPE) ?: WordleQuizType.TEXT.name
        val day = inputData.getString(INPUT_DAY)
        val mazeItemId = inputData.getString(INPUT_MAZE_TEM_ID)

        if (isLastRowCorrect) {
            UpdateGlobalEventDataWorker.enqueueWork(
                workManager = workManager,
                GameEvent.Wordle.GetWordCorrect
            )
        }

        wordleLoggingAnalytics.logGameEnd(
            wordLength = word.length,
            maxRows = rowLimit,
            lastRow = currentRowPosition,
            lastRowCorrect = isLastRowCorrect,
            quizType = quizType,
            day = day,
            mazeItemId = mazeItemId?.toIntOrNull()
        )

        if (mazeItemId != null) {
            mazeLoggingAnalytics.logMazeItemPlayed(isLastRowCorrect)
        }

        if (day != null) {
            saveDailyItemToCalendar(
                day = day.toLocalDate(),
                isCorrect = isLastRowCorrect,
                wordLength = word.length
            )

            wordleLoggingAnalytics.logDailyWordleItemComplete(word.length, day, isLastRowCorrect)
        }

        if (onlineServicesCore.connectionAvailable()) {
            val newXp = if (isLastRowCorrect) {
                wordleXpRepository.generateRandomXP(currentRowPosition)
            } else 0

            userRepository.updateLocalUserNewXPWordle(
                newXp = newXp.toLong(),
                wordsPlayed = 1,
                wordsCorrect = isLastRowCorrect.toLong()
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