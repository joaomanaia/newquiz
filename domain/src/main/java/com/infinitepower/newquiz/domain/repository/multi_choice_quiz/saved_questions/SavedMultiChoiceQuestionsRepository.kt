package com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.saved.SortSavedQuestionsBy
import kotlinx.coroutines.flow.Flow

interface SavedMultiChoiceQuestionsRepository {
    suspend fun insertQuestions(questions: List<MultiChoiceQuestion>)

    suspend fun insertQuestions(vararg questions: MultiChoiceQuestion)

    fun getFlowQuestions(
        sortBy: SortSavedQuestionsBy = SortSavedQuestionsBy.BY_DEFAULT
    ): Flow<List<MultiChoiceQuestion>>

    suspend fun getQuestions(): List<MultiChoiceQuestion>

    suspend fun deleteAllSelected(questions: List<MultiChoiceQuestion>)
}