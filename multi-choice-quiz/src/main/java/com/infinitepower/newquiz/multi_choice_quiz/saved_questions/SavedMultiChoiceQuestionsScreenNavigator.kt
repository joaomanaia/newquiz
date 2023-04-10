package com.infinitepower.newquiz.multi_choice_quiz.saved_questions

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion

interface SavedQuestionsScreenNavigator {
    fun navigateToMultiChoiceQuiz(initialQuestions: ArrayList<MultiChoiceQuestion>)
}

internal object SavedQuestionsScreenNavigatorPreview : SavedQuestionsScreenNavigator {
    override fun navigateToMultiChoiceQuiz(initialQuestions: ArrayList<MultiChoiceQuestion>) {
        println("Navigating to quick quiz")
    }
}