package com.infinitepower.newquiz.data.repository.question.saved_questions

import com.infinitepower.newquiz.domain.repository.question.saved_questions.SavedQuestionsDao
import com.infinitepower.newquiz.domain.repository.question.saved_questions.SavedQuestionsRepository
import com.infinitepower.newquiz.model.question.Question
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedQuestionsRepositoryImpl @Inject constructor(
    private val savedQuestionsDao: SavedQuestionsDao
) : SavedQuestionsRepository {
    override suspend fun insertQuestions(questions: List<Question>) {
        savedQuestionsDao.insertQuestions(questions)
    }

    override suspend fun insertQuestions(vararg questions: Question) {
        savedQuestionsDao.insertQuestions(*questions)
    }

    override fun getFlowQuestions(): Flow<List<Question>> = savedQuestionsDao.getFlowQuestions()

    override suspend fun getQuestions(): List<Question> = savedQuestionsDao.getQuestions()

    override suspend fun deleteAllSelected(questions: List<Question>) {
        savedQuestionsDao.deleteAll(questions)
    }
}