package com.infinitepower.newquiz.compose.domain.use_case.saved_questions

import com.infinitepower.newquiz.compose.domain.repository.saved_questions.SavedQuestionsRepository
import com.infinitepower.newquiz.compose.model.question.Question
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveQuestionUseCase @Inject constructor(
    private val savedQuestionsRepository: SavedQuestionsRepository
) {
    suspend operator fun invoke(question: Question) {
        savedQuestionsRepository.insertQuestions(question)
    }
}