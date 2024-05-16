package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.saved.SortSavedQuestionsBy

sealed interface SavedMultiChoiceQuestionsUiEvent {
    data class SelectQuestion(val question: MultiChoiceQuestion) : SavedMultiChoiceQuestionsUiEvent

    data object SelectAll : SavedMultiChoiceQuestionsUiEvent

    data object SelectNone : SavedMultiChoiceQuestionsUiEvent

    data object DeleteAllSelected : SavedMultiChoiceQuestionsUiEvent

    data object DownloadQuestions : SavedMultiChoiceQuestionsUiEvent

    @Keep
    data class SortQuestions(
        val sortBy: SortSavedQuestionsBy
    ) : SavedMultiChoiceQuestionsUiEvent
}
