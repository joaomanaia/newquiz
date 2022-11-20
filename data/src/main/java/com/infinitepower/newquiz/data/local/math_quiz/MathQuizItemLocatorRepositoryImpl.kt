package com.infinitepower.newquiz.data.local.math_quiz

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.domain.repository.math_quiz.MathQuizItemLocatorRepository
import com.infinitepower.newquiz.model.math_quiz.MathQuiz
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MathQuizItemLocatorRepositoryImpl @Inject constructor() : MathQuizItemLocatorRepository {
    override suspend fun getRandomQuestions(
        difficulty: QuestionDifficulty
    ): FlowResource<MathQuiz.HiddenItemLocator> = flow {
        try {
            emit(Resource.Loading())
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while getting random questions"))
        }
    }
}