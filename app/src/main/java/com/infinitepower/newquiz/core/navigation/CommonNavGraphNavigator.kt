package com.infinitepower.newquiz.core.navigation

import androidx.navigation.NavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.infinitepower.newquiz.maze_quiz.MazeScreenNavigator
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.saved_questions.SavedQuestionsScreenNavigator
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.navigation.navigate

class CommonNavGraphNavigator(
    private val navController: NavController
) : SavedQuestionsScreenNavigator, MazeScreenNavigator {
    private val remoteConfig by lazy { Firebase.remoteConfig }

    override fun navigateToQuickQuiz(initialQuestions: ArrayList<MultiChoiceQuestion>) {
        val remoteConfigDifficulty = remoteConfig.getString("multichoice_quickquiz_difficulty").run {
            if (this == "random") null else this
        }

        navController.navigate(
            MultiChoiceQuizScreenDestination(
                initialQuestions = initialQuestions,
                difficulty = remoteConfigDifficulty
            )
        )
    }

    override fun navigateToGame(item: MazeQuiz.MazeItem) {
        val destination = when (item) {
            is MazeQuiz.MazeItem.Wordle -> {
                WordleScreenDestination(
                    word = item.wordleWord.word,
                    quizType = item.wordleQuizType,
                    mazeItemId = item.id.toString(),
                    textHelper = item.wordleWord.textHelper
                )
            }
            is MazeQuiz.MazeItem.MultiChoice -> {
                MultiChoiceQuizScreenDestination(
                    initialQuestions = arrayListOf(item.question),
                    mazeItemId = item.id.toString()
                )
            }
        }

        navController.navigate(destination)
    }
}