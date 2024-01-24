package com.infinitepower.newquiz.domain.use_case.question

import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.model.FlowResource
import com.infinitepower.newquiz.model.Resource
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case to check if a question is locally saved.
 */
class IsQuestionSavedUseCase @Inject constructor(
    private val savedQuestionRepository: SavedMultiChoiceQuestionsRepository
) {
    operator fun invoke(question: MultiChoiceQuestion): FlowResource<Boolean> = flow {
        try {
            emit(Resource.Loading())

            // Get all the saved questions
            val savedQuestionsFlow = savedQuestionRepository.getFlowQuestions()

            savedQuestionsFlow.map { savedQuestions ->
                // Check if the question exists in the saved questions
                val questionExists = savedQuestions.any { savedQuestion ->
                    savedQuestion == question
                }

                Resource.Success(questionExists)
            }.also { questionExistsFlow -> emitAll(questionExistsFlow) }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(e.localizedMessage ?: "An unknown error occurred..."))
        }
    }
}
