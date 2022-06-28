package com.infinitepower.newquiz.domain.use_case.question

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.domain.repository.question.QuestionRepository
import com.infinitepower.newquiz.model.question.Question
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import androidx.annotation.IntRange

@Singleton
class GetRandomQuestionUseCase @Inject constructor(
    private val questionRepository: QuestionRepository
) {
    operator fun invoke(
        @IntRange(from = 0, to = 50) amount: Int = 10
    ): FlowResource<List<Question>> = flow {
        try {
            emit(Resource.Loading())

            val questions = questionRepository.getRandomQuestions(amount)
            emit(Resource.Success(questions))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "Error while loading questions"))
        }
    }
}