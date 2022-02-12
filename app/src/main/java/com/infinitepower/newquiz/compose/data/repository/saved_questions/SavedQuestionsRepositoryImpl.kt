package com.infinitepower.newquiz.compose.data.repository.saved_questions

import androidx.paging.PagingSource
import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.data.local.question.SavedQuestionDao
import com.infinitepower.newquiz.compose.domain.repository.saved_questions.SavedQuestionsRepository
import com.infinitepower.newquiz.compose.ui.saved_questions_list.SavedQuestionsListSortOrder

class SavedQuestionsRepositoryImpl(
    private val savedQuestionDao: SavedQuestionDao
) : SavedQuestionsRepository {
    override fun getAllQuestionsOrderByDescriptionPaging() = savedQuestionDao.getAllQuestionsOrderByDescriptionPaging()

    override fun getAllQuestionsOrderByTypePaging() = savedQuestionDao.getAllQuestionsOrderByTypePaging()

    override fun getAllQuestionsOrderByDifficultyPaging() = savedQuestionDao.getAllQuestionsOrderByDifficultyPaging()

    override suspend fun insertQuestions(vararg question: Question) {
        savedQuestionDao.insertQuestions(*question)
    }

    override suspend fun deleteQuestions(vararg question: Question) {
        savedQuestionDao.deleteQuestions(*question)
    }

    override suspend fun getAllQuestions() = savedQuestionDao.getAllQuestions()
}