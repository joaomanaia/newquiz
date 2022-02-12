package com.infinitepower.newquiz.compose.worker.quiz

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.compose.core.util.quiz.QuizXPUtil
import com.infinitepower.newquiz.compose.data.local.question.QuestionStep
import com.infinitepower.newquiz.compose.data.remote.user.UserApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@HiltWorker
class UpdateUserQuizXPWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userApi: UserApi,
    private val quizXPUtil: QuizXPUtil
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        /** Input data for receive test questions */
        const val NEW_XP_PARAM = "NEW_XP_PARAM"
        const val QUESTION_STEPS_STRING = "QUESTION_STEPS_STRING"
    }

    override suspend fun doWork(): Result {
        val newXP = inputData.getLong(NEW_XP_PARAM, -1)
        if (newXP <= 0) return Result.failure()

        val questionStepsString = inputData.getString(QUESTION_STEPS_STRING) ?: return Result.failure()
        val questionSteps = Json.decodeFromString<List<QuestionStep.Completed>>(questionStepsString)

        val correctAnswerRatio = getCorrectQuestionsRatio(questionSteps)
        val newXPBonus = getBonus(correctAnswerRatio)

        val finalXP = newXP + newXPBonus

        return try {
            userApi.tryAuthUpdateUserQuizXP(finalXP)
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun getCorrectQuestionsRatio(steps: List<QuestionStep.Completed>): Float {
        val totalQuestions = steps.size
        val correctQuestionNum = steps.count { step -> step.correct }
        return correctQuestionNum.toFloat() / totalQuestions.toFloat()
    }

    private fun getBonus(ratio: Float) = if (ratio == 1f) (quizXPUtil.getBonusAllQuestionsCorrectXp()) else 0
}