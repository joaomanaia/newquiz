package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion

sealed class SavedMultiChoiceQuestionsUiEvent {
    data class SelectQuestion(val question: MultiChoiceQuestion) : SavedMultiChoiceQuestionsUiEvent()

    object SelectAll : SavedMultiChoiceQuestionsUiEvent()

    object DeleteAllSelected : SavedMultiChoiceQuestionsUiEvent()

    object DownloadQuestions : SavedMultiChoiceQuestionsUiEvent()
}
