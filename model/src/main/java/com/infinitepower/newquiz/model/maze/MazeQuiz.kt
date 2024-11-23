package com.infinitepower.newquiz.model.maze

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.GameMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizQuestion
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceQuestion
import com.infinitepower.newquiz.model.question.QuestionDifficulty
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import com.infinitepower.newquiz.model.wordle.WordleWord
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Keep
data class MazeQuiz(
    val items: ImmutableList<MazeItem>
) {
    sealed interface MazeItem {
        val id: Int
        val mazeSeed: Int
        val difficulty: QuestionDifficulty
        val played: Boolean
        val gameMode: GameMode
        val categoryId: String

        @Keep
        data class Wordle(
            val wordleWord: WordleWord,
            val wordleQuizType: WordleQuizType,
            override val id: Int = 0,
            override val mazeSeed: Int,
            override val difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
            override val played: Boolean = false,
        ) : MazeItem {
            override val gameMode: GameMode = GameMode.WORDLE

            override val categoryId: String = wordleQuizType.name
        }

        @Keep
        data class MultiChoice(
            val question: MultiChoiceQuestion,
            override val id: Int = 0,
            override val mazeSeed: Int,
            override val difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
            override val played: Boolean = false
        ) : MazeItem {
            override val gameMode: GameMode = GameMode.MULTI_CHOICE

            override val categoryId: String = question.category.id
        }

        @Keep
        data class ComparisonQuiz(
            val question: ComparisonQuizQuestion,
            override val id: Int = 0,
            override val mazeSeed: Int,
            override val difficulty: QuestionDifficulty = QuestionDifficulty.Easy,
            override val played: Boolean = false
        ) : MazeItem {
            override val gameMode: GameMode = GameMode.COMPARISON_QUIZ

            override val categoryId: String = question.categoryId
        }
    }
}

fun emptyMaze(): MazeQuiz = MazeQuiz(items = persistentListOf())

/**
 * Check if an item at a given index in a list of MazeItem objects is playable.
 * An item is considered playable if it has not been played and the previous item has been played.
 *
 * @param index The index of the item to check.
 * @return true if the current item has not been played and either the previous item does not exist or has been played
 */
fun List<MazeQuiz.MazeItem>.isPlayableItem(index: Int): Boolean {
    // Check if the current item has been played. If it has, return false.
    if (isItemPlayed(index)) return false

    // If the current item is the first item in the list, return true.
    if (index == 0) return true

    // If the current item is not the first item in the list, check if the previous item has been played.
    // If it has, return true. Otherwise, return false.
    return isItemPlayed(index - 1)
}

/**
 * Returns true if the item at the given index has been played, false otherwise.
 *
 * @param index The index of the item to check.
 * @return True if the item has been played, false otherwise.
 */
infix fun List<MazeQuiz.MazeItem>.isItemPlayed(
    index: Int
): Boolean = getOrNull(index)?.played == true
