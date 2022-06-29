package com.infinitepower.newquiz.home_presentation

interface HomeScreenNavigator {
    fun navigateToQuickQuiz()

    fun navigateToSettings()
}

internal class HomeNavigatorPreviewImpl : HomeScreenNavigator {
    override fun navigateToQuickQuiz() {
        println("Navigating to Quick Quiz")
    }

    override fun navigateToSettings() {
        println("Navigating to Settings")
    }
}