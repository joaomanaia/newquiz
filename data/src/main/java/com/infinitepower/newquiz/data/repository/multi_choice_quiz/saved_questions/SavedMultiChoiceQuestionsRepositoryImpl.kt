package com.infinitepower.newquiz.data.repository.multi_choice_quiz.saved_questions

import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsDao
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedMultiChoiceQuestionsRepositoryImpl @Inject constructor(
    private val savedQuestionsDao: SavedMultiChoiceQuestionsDao
) : SavedMultiChoiceQuestionsRepository {
    override suspend fun insertQuestions(questions: List<MultiChoiceQuestion>) {
        savedQuestionsDao.insertQuestions(questions)
    }

    override suspend fun insertQuestions(vararg questions: MultiChoiceQuestion) {
        savedQuestionsDao.insertQuestions(*questions)
    }

    override fun getFlowQuestions(): Flow<List<MultiChoiceQuestion>> = savedQuestionsDao.getFlowQuestions()

    override suspend fun getQuestions(): List<MultiChoiceQuestion> = savedQuestionsDao.getQuestions()

    override suspend fun deleteAllSelected(questions: List<MultiChoiceQuestion>) {
        savedQuestionsDao.deleteAll(questions)
    }
}