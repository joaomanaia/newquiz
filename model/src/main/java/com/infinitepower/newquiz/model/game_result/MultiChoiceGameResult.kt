package com.infinitepower.newquiz.model.game_result

import androidx.annotation.Keep
import kotlinx.datetime.LocalDateTime

/**
 * This class is used to keep track of the user's games results and their scores
 * for each multiple choice game. It is used to display the user's scores
 * in the profile screen.
 *
 * @param gameId The id of the game.
 * @param correctAnswers The number of correct answers.
 * @param questionCount The number of questions.
 * @param averageAnswerTime The average time it took the user to answer a question.
 * @param earnedXp The amount of XP earned by the user for this game.
 * @param playedAt The date and time when the game was played.
 */
@Keep
data class MultiChoiceGameResult(
    val gameId: Int,
    val correctAnswers: UInt,
    val questionCount: UInt,
    val averageAnswerTime: Double,
    val earnedXp: UInt,
    val playedAt: LocalDateTime
) {
    init {
        require(correctAnswers <= questionCount) {
            "The number of correct answers cannot be greater than the number of questions."
        }
    }
}
