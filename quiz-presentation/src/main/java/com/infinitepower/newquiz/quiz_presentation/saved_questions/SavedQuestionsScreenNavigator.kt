package com.infinitepower.newquiz.quiz_presentation.saved_questions

import com.infinitepower.newquiz.model.question.Question

interface SavedQuestionsScreenNavigator {
    fun navigateToQuickQuiz(initialQuestions: ArrayList<Question>)
}

internal object SavedQuestionsScreenNavigatorPreview : SavedQuestionsScreenNavigator {
    override fun navigateToQuickQuiz(initialQuestions: ArrayList<Question>) {
        println("Navigating to quick quiz")
    }
}