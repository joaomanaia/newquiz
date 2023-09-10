package com.infinitepower.newquiz.data.repository.multi_choice_quiz.saved_questions

import com.infinitepower.newquiz.core.database.dao.SavedMultiChoiceQuestionsDao
import com.infinitepower.newquiz.core.database.model.MultiChoiceQuestionEntity
import com.infinitepower.newquiz.core.database.util.mappers.toEntity
import com.infinitepower.newquiz.core.database.util.mappers.toModel
import com.infinitepower.newquiz.domain.repository.multi_choice_quiz.saved_questions.SavedMultiChoiceQuestionsRepository
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.saved.SortSavedQuestionsBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedMultiChoiceQuestionsRepositoryImpl @Inject constructor(
    private val savedQuestionsDao: SavedMultiChoiceQuestionsDao
) : SavedMultiChoiceQuestionsRepository {
    override suspend fun insertQuestions(questions: List<MultiChoiceQuestion>) {
        val questionsEntity = questions.map(MultiChoiceQuestion::toEntity)
        savedQuestionsDao.insertQuestions(questionsEntity)
    }

    override suspend fun insertQuestions(vararg questions: MultiChoiceQuestion) {
        val questionsEntity = questions.map(MultiChoiceQuestion::toEntity)
        savedQuestionsDao.insertQuestions(questionsEntity)
    }

    override fun getFlowQuestions(
        sortBy: SortSavedQuestionsBy
    ): Flow<List<MultiChoiceQuestion>> {
        val questionsFlow = when (sortBy) {
            SortSavedQuestionsBy.BY_DEFAULT -> savedQuestionsDao.getFlowQuestions()
            SortSavedQuestionsBy.BY_DESCRIPTION -> savedQuestionsDao.getFlowQuestionsSortedByDescription()
            SortSavedQuestionsBy.BY_CATEGORY -> savedQuestionsDao.getFlowQuestionsSortedByCategory()
        }

        return questionsFlow.map { flowQuestions -> flowQuestions.map(com.infinitepower.newquiz.core.database.model.MultiChoiceQuestionEntity::toModel) }
    }

    override suspend fun getQuestions(): List<MultiChoiceQuestion> = savedQuestionsDao
        .getQuestions()
        .map(MultiChoiceQuestionEntity::toModel)

    override fun getCount(): Flow<Int> = savedQuestionsDao.getCount()

    override suspend fun deleteAllSelected(questions: List<MultiChoiceQuestion>) {
        val questionsEntity = questions.map(MultiChoiceQuestion::toEntity)
        savedQuestionsDao.deleteAll(questionsEntity)
    }
}