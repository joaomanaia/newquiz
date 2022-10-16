package com.infinitepower.newquiz.home_presentation

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion

interface HomeScreenNavigator {
    fun navigateToQuickQuiz(initialQuestions: ArrayList<MultiChoiceQuestion> = arrayListOf())

    fun navigateToFlagQuiz()

    fun navigateToSettings()

    fun navigateToSavedQuestions()

    fun navigateToWordle()
}

internal class HomeNavigatorPreviewImpl : HomeScreenNavigator {
    override fun navigateToQuickQuiz(initialQuestions: ArrayList<MultiChoiceQuestion>) {
        println("Navigating to Quick Quiz")
    }

    override fun navigateToFlagQuiz() {
        println("Navigating to Flag Quiz")
    }

    override fun navigateToSettings() {
        println("Navigating to Settings")
    }

    override fun navigateToSavedQuestions() {
        println("Navigating to Saved Questions")
    }

    override fun navigateToWordle() {
        println("Navigating to Wordle")
    }
}