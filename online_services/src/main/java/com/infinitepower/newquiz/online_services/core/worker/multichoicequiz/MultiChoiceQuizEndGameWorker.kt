package com.infinitepower.newquiz.online_services.core.worker.multichoicequiz

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.MultiChoiceQuizLoggingAnalytics
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.online_services.domain.game.xp.MultiChoiceQuizXPRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@HiltWorker
class MultiChoiceQuizEndGameWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val multiChoiceQuizLoggingAnalytics: MultiChoiceQuizLoggingAnalytics,
    private val multiChoiceQuizXPRepository: MultiChoiceQuizXPRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val INPUT_QUESTION_STEPS = "QUESTION_STEPS"
    }

    override suspend fun doWork(): Result {
        val questionStepsStr = inputData.getString(INPUT_QUESTION_STEPS) ?: return Result.failure()
        val questionSteps: List<MultiChoiceQuestionStep.Completed> = Json.decodeFromString(questionStepsStr)

        multiChoiceQuizLoggingAnalytics.logGameEnd(
            questionsSize = questionSteps.size,
            correctAnswers = questionSteps.count { it.correct }
        )

        return Result.success()
    }

    private suspend fun updateUserXp() {}
}