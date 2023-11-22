package com.infinitepower.newquiz.core.user_services.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.user_services.UserService
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.json.Json

@HiltWorker
class MultiChoiceQuizEndGameWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val analyticsHelper: AnalyticsHelper,
    private val userService: UserService
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val INPUT_QUESTION_STEPS = "QUESTION_STEPS"
        const val INPUT_GENERATE_XP = "GENERATE_XP"
    }

    override suspend fun doWork(): Result {
        val questionStepsStr = inputData.getString(INPUT_QUESTION_STEPS) ?: return Result.failure()
        val questionSteps: List<MultiChoiceQuestionStep.Completed> =
            Json.decodeFromString(questionStepsStr)

        val generateXp = inputData.getBoolean(INPUT_GENERATE_XP, true)

        analyticsHelper.logEvent(
            AnalyticsEvent.MultiChoiceGameEnd(
                questionsSize = questionSteps.size,
                correctAnswers = questionSteps.count { it.correct }
            )
        )

        userService.saveMultiChoiceGame(
            questionSteps = questionSteps,
            generateXp = generateXp
        )

        return Result.success()
    }
}