package com.infinitepower.newquiz.online_services.core.worker.multichoicequiz

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FieldValue
import com.infinitepower.newquiz.core.analytics.logging.multi_choice_quiz.MultiChoiceQuizLoggingAnalytics
import com.infinitepower.newquiz.core.common.database.DatabaseCommon
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestionStep
import com.infinitepower.newquiz.model.multi_choice_quiz.countCorrectQuestions
import com.infinitepower.newquiz.online_services.core.OnlineServicesCore
import com.infinitepower.newquiz.online_services.domain.game.xp.MultiChoiceQuizXPRepository
import com.infinitepower.newquiz.online_services.domain.user.UserRepository
import com.infinitepower.newquiz.online_services.model.user.User
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@HiltWorker
class MultiChoiceQuizEndGameWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val multiChoiceQuizLoggingAnalytics: MultiChoiceQuizLoggingAnalytics,
    private val multiChoiceQuizXPRepository: MultiChoiceQuizXPRepository,
    private val userRepository: UserRepository,
    private val onlineServicesCore: OnlineServicesCore
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val INPUT_QUESTION_STEPS = "QUESTION_STEPS"
        const val INPUT_SAVE_NEW_XP = "SAVE_NEW_XP"
    }

    override suspend fun doWork(): Result {
        val questionStepsStr = inputData.getString(INPUT_QUESTION_STEPS) ?: return Result.failure()
        val questionSteps: List<MultiChoiceQuestionStep.Completed> =
            Json.decodeFromString(questionStepsStr)

        multiChoiceQuizLoggingAnalytics.logGameEnd(
            questionsSize = questionSteps.size,
            correctAnswers = questionSteps.count { it.correct }
        )

        val saveNewXP = inputData.getBoolean(INPUT_SAVE_NEW_XP, true)

        if (onlineServicesCore.connectionAvailable() && saveNewXP) {
            val randomXP = multiChoiceQuizXPRepository.generateQuestionsRandomXP(questionSteps)

            val averageQuizTime = questionSteps.getAverageQuizTime()

            userRepository.updateLocalUserNewXP(
                newXp = randomXP.toLong(),
                averageQuizTime = averageQuizTime,
                totalQuestionsPlayed = questionSteps.count().toLong(),
                totalCorrectAnswers = questionSteps.countCorrectQuestions().toLong()
            )
        }

        return Result.success()
    }

    private fun List<MultiChoiceQuestionStep.Completed>.getAverageQuizTime(): Double {
        return map(MultiChoiceQuestionStep.Completed::questionTime).average()
    }
}