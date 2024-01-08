package com.infinitepower.newquiz.core.analytics

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.global_event.GameEvent

sealed class AnalyticsEvent(
    val type: String,
    val extras: Set<Param<*>> = emptySet()
) {
    @Keep
    data class Param<T>(
        val key: String,
        val value: T?
    )

    /**
     * Enum class for the different games that can be played.
     */
    enum class Game {
        MULTI_CHOICE_QUIZ,
        WORDLE,
        COMPARISON_QUIZ
    }

    // Core events

    @Keep
    data class CategoryClicked(
        val game: Game,
        val categoryId: String,
        val otherData: Map<String, Any?> = emptyMap()
    ) : AnalyticsEvent(
        type = "category_clicked",
        extras = setOf(
            Param("game", game.name.lowercase()),
            Param("id", categoryId)
        ) + otherData.map { Param(it.key, it.value) }
    )

    @Keep
    data class LevelUp(val level: Int) : AnalyticsEvent(
        type = "level_up",
        extras = setOf(
            Param("level", level),
            Param("character", "global")
        )
    )

    @Keep
    data class EarnDiamonds(val earned: Int) : AnalyticsEvent(
        type = "earn_virtual_currency",
        extras = setOf(
            Param("virtual_currency_name", "diamonds"),
            Param("value", earned)
        )
    )

    @Keep
    data class SpendDiamonds(val amount: Int, val usedFor: String) : AnalyticsEvent(
        type = "spend_virtual_currency",
        extras = setOf(
            Param("value", amount),
            Param("virtual_currency_name", "diamonds"),
            Param("item_name", usedFor)
        )
    )

    // Multi choice quiz events

    @Keep
    data class MultiChoiceGameStart(
        val questionsSize: Int,
        val category: String,
        val difficulty: String?,
        val mazeItemId: Int? = null
    ) : AnalyticsEvent(
        type = "multi_choice_game_start",
        extras = setOf(
            Param("questions_size", questionsSize),
            Param("category", category),
            Param("difficulty", difficulty),
            Param("maze_item_id", mazeItemId)
        )
    )

    @Keep
    data class MultiChoiceGameEnd(
        val questionsSize: Int,
        val correctAnswers: Int,
        val mazeItemId: Int? = null
    ) : AnalyticsEvent(
        type = "multi_choice_game_end",
        extras = setOf(
            Param("questions_size", questionsSize),
            Param("correct_answers", correctAnswers),
            Param("maze_item_id", mazeItemId)
        )
    )

    data object MultiChoiceSaveQuestion : AnalyticsEvent("multi_choice_save_question")

    data object MultiChoiceDownloadQuestions : AnalyticsEvent("multi_choice_download_questions")

    @Keep
    data class MultiChoicePlaySavedQuestions(
        val questionsSize: Int
    ) : AnalyticsEvent(
        type = "multi_choice_play_saved_questions",
        extras = setOf(
            Param("questions_size", questionsSize)
        )
    )

    // Wordle events

    @Keep
    data class WordleGameStart(
        val wordLength: Int,
        val maxRows: Int,
        val category: String,
        val mazeItemId: Int? = null
    ) : AnalyticsEvent(
        type = "wordle_game_start",
        extras = setOf(
            Param("word_length", wordLength),
            Param("max_rows", maxRows),
            Param("category", category),
            Param("maze_item_id", mazeItemId)
        )
    )

    @Keep
    data class WordleGameEnd(
        val wordLength: Int,
        val maxRows: Int,
        val lastRow: Int,
        val lastRowCorrect: Boolean,
        val category: String,
        val mazeItemId: Int? = null
    ) : AnalyticsEvent(
        type = "wordle_game_end",
        extras = setOf(
            Param("word_length", wordLength),
            Param("max_rows", maxRows),
            Param("last_row", lastRow),
            Param("last_row_correct", lastRowCorrect),
            Param("category", category),
            Param("maze_item_id", mazeItemId)
        )
    )

    // Maze events

    @Keep
    data class CreateMaze(
        val seed: Int,
        val questionsSize: Int
    ) : AnalyticsEvent(
        type = "create_maze",
        extras = setOf(
            Param("seed", seed),
            Param("questions_size", questionsSize)
        )
    )

    data object RestartMaze : AnalyticsEvent("restart_maze")

    @Keep
    data class MazeItemClick(val index: Int) : AnalyticsEvent(
        type = "maze_item_click",
        extras = setOf(
            Param("index", index)
        )
    )

    @Keep
    data class MazeItemPlayed(val correct: Boolean) : AnalyticsEvent(
        type = "maze_item_played",
        extras = setOf(
            Param("correct", correct)
        )
    )

    @Keep
    data class MazeCompleted(val questionSize: Int) : AnalyticsEvent(
        type = "maze_completed",
        extras = setOf(
            Param("question_size", questionSize)
        )
    )

    // Comparison Quiz

    @Keep
    data class ComparisonQuizGameStart(
        val category: String,
        val comparisonMode: String,
    ) : AnalyticsEvent(
        type = "comparison_quiz_game_start",
        extras = setOf(
            Param("category", category),
            Param("comparison_mode", comparisonMode)
        )
    )

    @Keep
    data class ComparisonQuizGameEnd(
        val category: String?,
        val comparisonMode: String?,
        val score: Int
    ) : AnalyticsEvent(
        type = "comparison_quiz_game_end",
        extras = setOf(
            Param("category", category),
            Param("comparison_mode", comparisonMode),
            Param("score", score)
        )
    )

    // Daily challenge
    @Keep
    data class DailyChallengeItemClick(
        val event: GameEvent
    ) : AnalyticsEvent(
        type = "daily_challenge_item_click",
        extras = setOf(
            Param("event", event.toString())
        )
    )

    @Keep
    data class DailyChallengeItemClaim(
        val event: GameEvent,
        val steps: Int
    ) : AnalyticsEvent(
        type = "daily_challenge_item_claim",
        extras = setOf(
            Param("event", event.toString()),
            Param("steps", steps)
        )
    )
}
