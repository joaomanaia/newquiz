package com.infinitepower.newquiz.quiz_presentation.saved_questions

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.question.Question

@Keep
data class SavedQuestionsUiState(
    val questions: List<Question> = emptyList(),
    val selectedQuestions: List<Question> = emptyList()
) {
    val arrayListSelectedQuestions: ArrayList<Question>
        get() = ArrayList(selectedQuestions.takeLast(100))

    fun randomQuestions(limit: Int = 5): ArrayList<Question> {
        val randomQuestions = questions.shuffled().take(limit)
        return ArrayList(randomQuestions)
    }
}