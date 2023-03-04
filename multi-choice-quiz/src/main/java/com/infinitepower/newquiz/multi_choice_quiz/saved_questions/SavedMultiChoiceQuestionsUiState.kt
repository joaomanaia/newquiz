package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion

@Keep
data class SavedMultiChoiceQuestionsUiState(
    val questions: List<MultiChoiceQuestion> = emptyList(),
    val selectedQuestions: List<MultiChoiceQuestion> = emptyList()
) {
    fun randomQuestions(limit: Int = 5): List<MultiChoiceQuestion> {
        return questions.shuffled().take(limit)
    }
}