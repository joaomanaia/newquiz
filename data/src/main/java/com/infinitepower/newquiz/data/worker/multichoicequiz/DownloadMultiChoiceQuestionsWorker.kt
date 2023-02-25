package com.infinitepower.newquiz.data.worker.multichoicequiz

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DownloadMultiChoiceQuestionsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val savedQuestionsRepository: SavedMultiChoiceQuestionsRepository,
    private val questionRepository: MultiChoiceQuestionRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val allSavedQuestions = savedQuestionsRepository.getQuestions()

        val questions = questionRepository
            .getRandomQuestions(amount = 50, category = MultiChoiceBaseCategory.Normal())
            .filter { it !in allSavedQuestions }

        savedQuestionsRepository.insertQuestions(questions)

        return Result.success()
    }
}
