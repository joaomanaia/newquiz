package com.infinitepower.newquiz.compose.domain.use_case.saved_questions

import com.infinitepower.newquiz.compose.core.common.Resource
import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.domain.repository.saved_questions.SavedQuestionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllSavedQuestionsUseCase @Inject constructor(
    private val savedQuestionsRepository: SavedQuestionsRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Question>>> = flow {
        try {
            emit(Resource.Loading())
            val allQuestions = savedQuestionsRepository.getAllQuestions()
            emit(Resource.Success(data = allQuestions))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = e.localizedMessage ?: "Error"))
        }
    }
}