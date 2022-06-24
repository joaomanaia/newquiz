package com.infinitepower.newquiz.compose.worker.quiz

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.infinitepower.newquiz.compose.model.question.QuestionStep
import com.infinitepower.newquiz.compose.domain.use_case.question.GetQuestions
import com.infinitepower.newquiz.compose.model.question.Question
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@HiltWorker
class GetQuizDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getQuestions: GetQuestions,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        /** Input data for receive test questions */
        const val TEST_QUESTIONS_ENABLED_PARAM = "TEST_QUESTIONS_ENABLED"
        const val INITIAL_QUESTIONS_PARAM = "INITIAL_QUESTIONS"

        const val OUT_QUESTIONS_PARAM = "OUT_QUESTIONS"
        const val OUT_QUESTIONS_STEP_PARAM = "OUT_QUESTIONS_STEP"
    }

    override suspend fun doWork(): Result {
        try {
            val testQuestionsEnabled = inputData.getBoolean(TEST_QUESTIONS_ENABLED_PARAM, true)

            val initialQuestionsString = inputData.getString(INITIAL_QUESTIONS_PARAM) ?: ""

            val initialQuestions = Json.decodeFromString<List<Question>>(initialQuestionsString)

            val questions = initialQuestions.ifEmpty { getQuestions(testQuestionsEnabled) }

            val questionsString = Json.encodeToString(questions)

            val questionSteps = questions.map { question ->
                QuestionStep.NotCurrent(question = question)
            }
            val questionsStepsString = Json.encodeToString(questionSteps)

            val output = workDataOf(
                OUT_QUESTIONS_PARAM to questionsString,
                OUT_QUESTIONS_STEP_PARAM to questionsStepsString
            )

            return Result.success(output)
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }
    }
}