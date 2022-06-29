package com.infinitepower.newquiz.core.navigation

import androidx.navigation.NavController
import com.infinitepower.newquiz.quiz_presentation.QuizType
import com.infinitepower.newquiz.quiz_presentation.destinations.QuizScreenDestination
import com.infinitepower.newquiz.home_presentation.HomeScreenNavigator
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.navigation.navigate

class CommonNavGraphNavigator(
    private val navController: NavController
) : HomeScreenNavigator {
    override fun navigateToQuickQuiz() {
        navController.navigate(QuizScreenDestination(quizType = QuizType.QUICK_QUIZ))
    }

    override fun navigateToSettings() {
        navController.navigate(SettingsScreenDestination())
    }
}