package com.infinitepower.newquiz.core.navigation

import androidx.navigation.NavController
import com.infinitepower.newquiz.quiz_presentation.destinations.QuizScreenDestination
import com.infinitepower.newquiz.home_presentation.HomeScreenNavigator
import com.infinitepower.newquiz.model.question.Question
import com.infinitepower.newquiz.quiz_presentation.destinations.SavedQuestionsScreenDestination
import com.infinitepower.newquiz.quiz_presentation.saved_questions.SavedQuestionsScreenNavigator
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.navigation.navigate

class CommonNavGraphNavigator(
    private val navController: NavController
) : HomeScreenNavigator, SavedQuestionsScreenNavigator {
    override fun navigateToQuickQuiz(initialQuestions: ArrayList<Question>) {
        navController.navigate(QuizScreenDestination(initialQuestions = initialQuestions))
    }

    override fun navigateToSettings() {
        navController.navigate(SettingsScreenDestination())
    }

    override fun navigateToSavedQuestions() {
        navController.navigate(SavedQuestionsScreenDestination)
    }

    override fun navigateToWordle() {
        navController.navigate(WordleScreenDestination())
    }
}