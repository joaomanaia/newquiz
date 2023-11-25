package com.infinitepower.newquiz.wordle.util.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.data.worker.UpdateGlobalEventDataWorker
import com.infinitepower.newquiz.domain.repository.home.RecentCategoriesRepository
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WordleEndGameWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val workManager: WorkManager,
    private val recentCategoriesRepository: RecentCategoriesRepository,
    private val analyticsHelper: AnalyticsHelper,
    private val userService: UserService
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val INPUT_WORD = "word"
        const val INPUT_ROW_LIMIT = "row_limit"
        const val INPUT_CURRENT_ROW_POSITION = "current_row_position"
        const val INPUT_IS_LAST_ROW_CORRECT = "is_last_row_correct"
        const val INPUT_QUIZ_TYPE = "quiz_type"
        const val INPUT_MAZE_TEM_ID = "maze_item_id"
    }

    override suspend fun doWork(): Result {
        val word = inputData.getString(INPUT_WORD) ?: return Result.failure()
        val rowLimit = inputData.getInt(INPUT_ROW_LIMIT, 0)
        val currentRowPosition = inputData.getInt(INPUT_CURRENT_ROW_POSITION, 0)
        val isLastRowCorrect = inputData.getBoolean(INPUT_IS_LAST_ROW_CORRECT, false)
        val quizTypeName = inputData.getString(INPUT_QUIZ_TYPE) ?: WordleQuizType.TEXT.name
        val mazeItemId = inputData.getString(INPUT_MAZE_TEM_ID)

        recentCategoriesRepository.addWordleCategory(quizTypeName)

        if (isLastRowCorrect) {
            UpdateGlobalEventDataWorker.enqueueWork(
                workManager = workManager,
                GameEvent.Wordle.GetWordCorrect
            )
        }

        analyticsHelper.logEvent(
            AnalyticsEvent.WordleGameEnd(
                wordLength = word.length,
                maxRows = rowLimit,
                lastRow = currentRowPosition,
                lastRowCorrect = isLastRowCorrect,
                category = quizTypeName,
                mazeItemId = mazeItemId?.toIntOrNull()
            )
        )

        if (mazeItemId != null) {
            analyticsHelper.logEvent(AnalyticsEvent.MazeItemPlayed(isLastRowCorrect))
        }

        if (isLastRowCorrect) {
            userService.saveWordleGame(
                wordLength = word.length.toUInt(),
                rowsUsed = currentRowPosition.toUInt(),
                maxRows = rowLimit,
                categoryId = quizTypeName,
                generateXp = true
            )
        }

        return Result.success()
    }
}