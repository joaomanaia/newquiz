package com.infinitepower.newquiz.online_services.core.worker.multichoicequiz

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.core.analytics.AnalyticsEvent
import com.infinitepower.newquiz.core.analytics.AnalyticsHelper
import com.infinitepower.newquiz.core.network.NetworkStatusTracker
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.countCorrectQuestions
import com.infinitepower.newquiz.online_services.domain.game.xp.MultiChoiceQuizXPRepository
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.json.Json

@HiltWorker
class MultiChoiceQuizEndGameWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val multiChoiceQuizXPRepository: MultiChoiceQuizXPRepository,
    private val userRepository: UserRepository,
    private val networkStatusTracker: NetworkStatusTracker,
    private val analyticsHelper: AnalyticsHelper
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val INPUT_QUESTION_STEPS = "QUESTION_STEPS"
        const val INPUT_SAVE_NEW_XP = "SAVE_NEW_XP"
    }

    override suspend fun doWork(): Result {
        val questionStepsStr = inputData.getString(INPUT_QUESTION_STEPS) ?: return Result.failure()
        val questionSteps: List<MultiChoiceQuestionStep.Completed> =
            Json.decodeFromString(questionStepsStr)

        analyticsHelper.logEvent(
            AnalyticsEvent.MultiChoiceGameEnd(
                questionsSize = questionSteps.size,
                correctAnswers = questionSteps.count { it.correct }
            )
        )

        val saveNewXP = inputData.getBoolean(INPUT_SAVE_NEW_XP, true)

        if (networkStatusTracker.isCurrentlyConnected() && saveNewXP) {
            val randomXP = multiChoiceQuizXPRepository.generateQuestionsRandomXP(questionSteps)

            val averageQuizTime = questionSteps.getAverageQuizTime()

            userRepository.updateLocalUser(
                newXp = randomXP.toULong(),
                averageQuizTime = averageQuizTime,
                totalQuestionsPlayed = questionSteps.count().toULong(),
                totalCorrectAnswers = questionSteps.countCorrectQuestions().toULong()
            )
        }

        return Result.success()
    }

    private fun List<MultiChoiceQuestionStep.Completed>.getAverageQuizTime(): Double {
        return map(MultiChoiceQuestionStep.Completed::questionTime).average()
    }
}