package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion

@Keep
data class SavedMultiChoiceQuestionsUiState(
    val questions: List<MultiChoiceQuestion> = emptyList(),
    val selectedQuestions: List<MultiChoiceQuestion> = emptyList()
) {
    val arrayListSelectedQuestions: ArrayList<MultiChoiceQuestion>
        get() = ArrayList(selectedQuestions.takeLast(100))

    fun randomQuestions(limit: Int = 5): ArrayList<MultiChoiceQuestion> {
        val randomQuestions = questions.shuffled().take(limit)
        return ArrayList(randomQuestions)
    }
}