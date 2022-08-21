package com.infinitepower.newquiz.domain.use_case.question

import com.infinitepower.newquiz.core.common.FlowResource
import com.infinitepower.newquiz.core.common.Resource
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.MultiChoiceQuestionRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import androidx.annotation.IntRange

@Singleton
class GetRandomMultiChoiceQuestionUseCase @Inject constructor(
    private val questionRepository: MultiChoiceQuestionRepository
) {
    operator fun invoke(
        @IntRange(from = 0, to = 50) amount: Int = 10
    ): FlowResource<List<MultiChoiceQuestion>> = flow {
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