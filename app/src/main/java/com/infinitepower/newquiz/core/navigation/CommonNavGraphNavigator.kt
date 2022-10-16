package com.infinitepower.newquiz.core.navigation

import androidx.navigation.NavController
import com.infinitepower.newquiz.core.multi_choice_quiz.MultiChoiceQuizType
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.home_presentation.HomeScreenNavigator
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.multi_choice_quiz.destinations.SavedMultiChoiceQuestionsScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.saved_questions.SavedQuestionsScreenNavigator
import com.infinitepower.newquiz.settings_presentation.destinations.SettingsScreenDestination
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.navigation.navigate

class CommonNavGraphNavigator(
    private val navController: NavController
) : HomeScreenNavigator, SavedQuestionsScreenNavigator {
    override fun navigateToQuickQuiz(initialQuestions: ArrayList<MultiChoiceQuestion>) {
        navController.navigate(MultiChoiceQuizScreenDestination(initialQuestions = initialQuestions))
    }

    override fun navigateToFlagQuiz() {
        navController.navigate(MultiChoiceQuizScreenDestination(type = MultiChoiceQuizType.FLAG))
    }

    override fun navigateToSettings() {
        navController.navigate(SettingsScreenDestination())
    }

    override fun navigateToSavedQuestions() {
        navController.navigate(SavedMultiChoiceQuestionsScreenDestination)
    }

    override fun navigateToWordle() {
        navController.navigate(WordleScreenDestination())
    }
}