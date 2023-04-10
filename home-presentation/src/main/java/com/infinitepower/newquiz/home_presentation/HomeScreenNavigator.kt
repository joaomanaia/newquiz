package com.infinitepower.newquiz.home_presentation

import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion

interface HomeScreenNavigator {
    fun navigateToMaze()

    fun navigateToMultiChoiceQuiz(
        initialQuestions: ArrayList<MultiChoiceQuestion> = ArrayList()
    )

    fun navigateToWordleQuiz()
}

object EmptyHomeScreenNavigator : HomeScreenNavigator {
    override fun navigateToMaze() {
        println("Navigating to maze")
    }

    override fun navigateToMultiChoiceQuiz(initialQuestions: ArrayList<MultiChoiceQuestion>) {
        println("Navigating to multi choice quiz with initial questions: $initialQuestions")
    }

    override fun navigateToWordleQuiz() {
        println("Navigating to wordle quiz")
    }
}
