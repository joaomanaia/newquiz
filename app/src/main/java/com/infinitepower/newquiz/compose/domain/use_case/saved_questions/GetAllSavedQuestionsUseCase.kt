package com.infinitepower.newquiz.compose.domain.use_case.saved_questions

import com.infinitepower.newquiz.compose.domain.repository.saved_questions.SavedQuestionsRepository
import com.infinitepower.newquiz.compose.model.question.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllSavedQuestionsUseCase @Inject constructor(
    private val savedQuestionsRepository: SavedQuestionsRepository
) {
    suspend operator fun invoke(): Flow<com.infinitepower.newquiz.compose.core.common.Resource<List<Question>>> = flow {
        try {
            emit(com.infinitepower.newquiz.compose.core.common.Resource.Loading())
            val allQuestions = savedQuestionsRepository.getAllQuestions()
            emit(com.infinitepower.newquiz.compose.core.common.Resource.Success(data = allQuestions))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(com.infinitepower.newquiz.compose.core.common.Resource.Error(message = e.localizedMessage ?: "Error"))
        }
    }
}