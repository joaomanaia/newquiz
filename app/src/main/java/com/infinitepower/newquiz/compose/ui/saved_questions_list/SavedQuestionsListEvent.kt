package com.infinitepower.newquiz.compose.ui.saved_questions_list

import com.infinitepower.newquiz.compose.model.question.Question

sealed class SavedQuestionsListEvent {
    data class OnDeleteQuestionClick(val question: Question) : SavedQuestionsListEvent()

    object OnUndoDeleteClick : SavedQuestionsListEvent()

    data class OnQuestionClick(val question: Question) : SavedQuestionsListEvent()

    object OnSelectAllClick : SavedQuestionsListEvent()

    object OnUnselectAllClick : SavedQuestionsListEvent()

    object OnDeleteAllSelectedClick : SavedQuestionsListEvent()

    data class OnSortOrderChange(
        val order: SavedQuestionsListSortOrder
    ) : SavedQuestionsListEvent()

    object OnPlayQuizGame : SavedQuestionsListEvent()
}
