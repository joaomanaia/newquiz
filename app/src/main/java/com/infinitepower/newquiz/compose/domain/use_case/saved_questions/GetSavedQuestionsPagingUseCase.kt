package com.infinitepower.newquiz.compose.domain.use_case.saved_questions

import androidx.paging.PagingSource
import com.infinitepower.newquiz.compose.core.common.Resource
import com.infinitepower.newquiz.compose.data.local.question.Question
import com.infinitepower.newquiz.compose.domain.repository.saved_questions.SavedQuestionsRepository
import com.infinitepower.newquiz.compose.ui.saved_questions_list.SavedQuestionsListSortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSavedQuestionsPagingUseCase @Inject constructor(
    private val savedQuestionsRepository: SavedQuestionsRepository
) {
    operator fun invoke(
        sortOrder: SavedQuestionsListSortOrder
    ): PagingSource<Int, Question> = when(sortOrder) {
        SavedQuestionsListSortOrder.BY_DESCRIPTION -> savedQuestionsRepository.getAllQuestionsOrderByDescriptionPaging()
        SavedQuestionsListSortOrder.BY_TYPE -> savedQuestionsRepository.getAllQuestionsOrderByTypePaging()
        SavedQuestionsListSortOrder.BY_DIFFICULTY -> savedQuestionsRepository.getAllQuestionsOrderByDifficultyPaging()
    }
}