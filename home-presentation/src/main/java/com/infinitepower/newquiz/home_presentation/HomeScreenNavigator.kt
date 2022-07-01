package com.infinitepower.newquiz.home_presentation

import com.infinitepower.newquiz.model.question.Question

interface HomeScreenNavigator {
    fun navigateToQuickQuiz(initialQuestions: ArrayList<Question> = arrayListOf())

    fun navigateToSettings()

    fun navigateToSavedQuestions()
}

internal class HomeNavigatorPreviewImpl : HomeScreenNavigator {
    override fun navigateToQuickQuiz(initialQuestions: ArrayList<Question>) {
        println("Navigating to Quick Quiz")
    }

    override fun navigateToSettings() {
        println("Navigating to Settings")
    }

    override fun navigateToSavedQuestions() {
        println("Navigating to Saved Questions")
    }
}