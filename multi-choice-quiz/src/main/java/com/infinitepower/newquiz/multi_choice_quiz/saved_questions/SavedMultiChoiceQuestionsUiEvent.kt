package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.saved.SortSavedQuestionsBy

sealed class SavedMultiChoiceQuestionsUiEvent {
    data class SelectQuestion(val question: MultiChoiceQuestion) : SavedMultiChoiceQuestionsUiEvent()

    object SelectAll : SavedMultiChoiceQuestionsUiEvent()

    object DeleteAllSelected : SavedMultiChoiceQuestionsUiEvent()

    object DownloadQuestions : SavedMultiChoiceQuestionsUiEvent()

    @Keep
    data class SortQuestions(
        val sortBy: SortSavedQuestionsBy
    ) : SavedMultiChoiceQuestionsUiEvent()
}
