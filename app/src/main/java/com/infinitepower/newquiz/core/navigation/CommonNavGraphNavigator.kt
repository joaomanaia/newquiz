package com.infinitepower.newquiz.core.navigation

import androidx.navigation.NavController
import com.infinitepower.newquiz.comparison_quiz.destinations.ComparisonQuizScreenDestination
import com.infinitepower.newquiz.core.remote_config.RemoteConfig
import com.infinitepower.newquiz.core.remote_config.RemoteConfigValue
import com.infinitepower.newquiz.core.remote_config.get
import com.infinitepower.newquiz.data.util.mappers.comparisonquiz.toEntity
import com.infinitepower.newquiz.feature.daily_challenge.DailyChallengeScreenNavigator
import com.infinitepower.newquiz.feature.maze.destinations.LevelResultsScreenDestination
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizItem
import com.infinitepower.newquiz.model.maze.MazeQuiz
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceBaseCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.multi_choice_quiz.destinations.MultiChoiceQuizScreenDestination
import com.infinitepower.newquiz.multi_choice_quiz.saved_questions.SavedQuestionsScreenNavigator
import com.infinitepower.newquiz.wordle.destinations.WordleScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.destination

class CommonNavGraphNavigator(
    private val navigator: DestinationsNavigator,
    private val navController: NavController,
    private val remoteConfig: RemoteConfig
) : SavedQuestionsScreenNavigator,
    MazeNavigator,
    DailyChallengeScreenNavigator {

    override fun navigateToWordleQuiz(type: WordleQuizType) {
        navigator.navigate(WordleScreenDestination(quizType = type))
    }

    override fun navigateToComparisonQuiz(
        category: ComparisonQuizCategory,
        mode: ComparisonMode
    ) {
        navigator.navigate(ComparisonQuizScreenDestination(category.id, mode))
    }

    override fun navigateToMultiChoiceQuiz(initialQuestions: ArrayList<MultiChoiceQuestion>) {
        val remoteConfigDifficulty = remoteConfig
            .get(RemoteConfigValue.MULTICHOICE_QUICKQUIZ_DIFFICULTY)
            .run {
                if (this == "random") null else this
            }

        navigator.navigate(
            MultiChoiceQuizScreenDestination(
                initialQuestions = initialQuestions,
                difficulty = remoteConfigDifficulty
            )
        )
    }

    override fun navigateToMultiChoiceQuiz(category: MultiChoiceBaseCategory) {
        val remoteConfigDifficulty = remoteConfig
            .get(RemoteConfigValue.MULTICHOICE_QUICKQUIZ_DIFFICULTY)
            .run {
                if (this == "random") null else this
            }

        navigator.navigate(
            MultiChoiceQuizScreenDestination(
                difficulty = remoteConfigDifficulty,
                category = category
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

            is MazeQuiz.MazeItem.ComparisonQuiz -> {
                val initialItems = item.question.questions
                    .toList()
                    .map(ComparisonQuizItem::toEntity)

                ComparisonQuizScreenDestination(
                    categoryId = item.question.categoryId,
                    initialItems = ArrayList(initialItems),
                    mazeItemId = item.id
                )
            }
        }

        navigator.navigate(destination) {
            popUpTo(LevelResultsScreenDestination) {
                inclusive = true
            }
        }
    }

    override fun navigateToMazeResults(itemId: Int) {
        navigator.navigate(LevelResultsScreenDestination(itemId)) {
            launchSingleTop = true

            // Remove current screen from back stack
            val currentDestination = navController.currentBackStackEntry?.destination()
            if (currentDestination != null) {
                popUpTo(currentDestination) {
                    inclusive = true
                }
            }
        }
    }
}
