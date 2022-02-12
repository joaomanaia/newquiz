package com.infinitepower.newquiz.compose.domain.repository.saved_questions

import androidx.paging.PagingSource
import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.ui.saved_questions_list.SavedQuestionsListSortOrder

interface SavedQuestionsRepository {
    fun getAllQuestionsOrderByDescriptionPaging(): PagingSource<Int, Question>

    fun getAllQuestionsOrderByTypePaging(): PagingSource<Int, Question>

    fun getAllQuestionsOrderByDifficultyPaging(): PagingSource<Int, Question>

    suspend fun insertQuestions(vararg question: Question)

    suspend fun deleteQuestions(vararg question: Question)

    suspend fun getAllQuestions(): List<Question>
}