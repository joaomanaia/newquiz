package com.infinitepower.newquiz.home_presentation

interface HomeNavigator {
    fun navigateToQuickQuiz()
}

internal class HomeNavigatorPreviewImpl : HomeNavigator {
    override fun navigateToQuickQuiz() {
        println("Navigating to Quick Quiz")
    }
}