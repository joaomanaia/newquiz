package com.infinitepower.newquiz.quiz_presentation.saved_questions

import com.infinitepower.newquiz.model.question.Question

sealed class SavedQuestionsUiEvent {
    data class SelectQuestion(val question: Question) : SavedQuestionsUiEvent()

    object SelectAll : SavedQuestionsUiEvent()

    object DeleteAllSelected : SavedQuestionsUiEvent()

    object DownloadQuestions : SavedQuestionsUiEvent()
}
