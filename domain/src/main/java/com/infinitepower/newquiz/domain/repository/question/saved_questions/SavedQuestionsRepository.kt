package com.infinitepower.newquiz.domain.repository.question.saved_questions

import com.infinitepower.newquiz.model.question.Question
import kotlinx.coroutines.flow.Flow

interface SavedQuestionsRepository {
    suspend fun insertQuestions(questions: List<Question>)

    suspend fun insertQuestions(vararg questions: Question)

    fun getFlowQuestions(): Flow<List<Question>>

    suspend fun getQuestions(): List<Question>

    suspend fun deleteAllSelected(questions: List<Question>)
}