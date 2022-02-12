package com.infinitepower.newquiz.compose.domain.use_case.saved_questions

import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.domain.repository.saved_questions.SavedQuestionsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteSavedQuestionUseCase @Inject constructor(
    private val savedQuestionsRepository: SavedQuestionsRepository
) {
    suspend operator fun invoke(vararg question: Question) {
        savedQuestionsRepository.deleteQuestions(*question)
    }
}