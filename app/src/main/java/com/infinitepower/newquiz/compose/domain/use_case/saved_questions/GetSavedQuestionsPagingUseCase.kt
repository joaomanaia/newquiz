package com.infinitepower.newquiz.compose.domain.use_case.saved_questions

import androidx.paging.PagingSource
import com.infinitepower.newquiz.compose.domain.repository.saved_questions.SavedQuestionsRepository
import com.infinitepower.newquiz.compose.model.question.Question
import com.infinitepower.newquiz.compose.ui.saved_questions_list.SavedQuestionsListSortOrder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSavedQuestionsPagingUseCase @Inject constructor(
    private val savedQuestionsRepository: SavedQuestionsRepository
) {
    operator fun invoke(
        sortOrder: SavedQuestionsListSortOrder
    ): PagingSource<Int, com.infinitepower.newquiz.compose.model.question.Question> = when(sortOrder) {
        SavedQuestionsListSortOrder.BY_DESCRIPTION -> savedQuestionsRepository.getAllQuestionsOrderByDescriptionPaging()
        SavedQuestionsListSortOrder.BY_TYPE -> savedQuestionsRepository.getAllQuestionsOrderByTypePaging()
        SavedQuestionsListSortOrder.BY_DIFFICULTY -> savedQuestionsRepository.getAllQuestionsOrderByDifficultyPaging()
    }
}