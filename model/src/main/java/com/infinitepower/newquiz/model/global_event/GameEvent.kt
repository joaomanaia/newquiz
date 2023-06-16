package com.infinitepower.newquiz.model.global_event

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonMode
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import com.infinitepower.newquiz.model.wordle.WordleQuizType
import kotlin.random.Random

sealed class GameEvent(
    val key: String,
    val valueRange: UIntProgression
) {
    override fun toString(): String = key

    companion object {
        private fun eventsWithoutArgs(): List<GameEvent> = listOf(
            // Multi choice
            MultiChoice.PlayRandomQuiz,
            MultiChoice.EndQuiz,
            MultiChoice.PlayQuestions,
            MultiChoice.GetAnswersCorrect,
            // Wordle
            Wordle.GetWordCorrect
        )

        fun fromKey(key: String): GameEvent {
            val eventsWithoutArgs = eventsWithoutArgs()

            return when {
                // Multi choice play quiz with category
                key.startsWith(MultiChoice.PlayQuizWithCategory.KEY_PREFIX) -> {
                    val categoryKey = key.substringAfter(MultiChoice.PlayQuizWithCategory.KEY_PREFIX)

                    MultiChoice.PlayQuizWithCategory(categoryKey)
                }
                // Wordle play word with category
                key.startsWith(Wordle.PlayWordWithCategory.KEY_PREFIX) -> {
                    val categoryKey = key.substringAfter(Wordle.PlayWordWithCategory.KEY_PREFIX)

                    Wordle.PlayWordWithCategory(WordleQuizType.valueOf(categoryKey))
                }
                // Comparison quiz play quiz with category
                key.startsWith(ComparisonQuiz.PlayQuizWithCategory.KEY_PREFIX) -> {
                    val categoryId = key.substringAfter(ComparisonQuiz.PlayQuizWithCategory.KEY_PREFIX)

                    ComparisonQuiz.PlayQuizWithCategory(categoryId)
                }
                // Comparison quiz play quiz with mode
                key.startsWith(ComparisonQuiz.PlayWithComparisonMode.KEY_PREFIX) -> {
                    val modeId = key.substringAfter(ComparisonQuiz.PlayWithComparisonMode.KEY_PREFIX)

                    ComparisonQuiz.PlayWithComparisonMode(ComparisonMode.valueOf(modeId))
                }
                // Comparison quiz play and get score
                key.startsWith(ComparisonQuiz.PlayAndGetScore.KEY_PREFIX) -> {
                    val score = key.substringAfter(ComparisonQuiz.PlayAndGetScore.KEY_PREFIX).toInt()

                    ComparisonQuiz.PlayAndGetScore(score)
                }
                // Other events without args
                eventsWithoutArgs.any { it.key == key } -> eventsWithoutArgs.first { it.key == key }
                else -> throw IllegalArgumentException("Unknown key: $key")
            }
        }

        fun getRandomEvents(
            count: Int,
            multiChoiceCategories: List<MultiChoiceCategory>,
            comparisonQuizCategories: List<ComparisonQuizCategory>,
            random: Random = Random
        ): List<GameEvent> {
            val multiChoicePlayQuizWithCategory = MultiChoice.PlayQuizWithCategory(
                categoryId = multiChoiceCategories.random(random).id
            )

            val wordleWithType = Wordle.PlayWordWithCategory(
                wordleCategory = WordleQuizType.values().random(random)
            )

            val comparisonPlayWithCategory = ComparisonQuiz.PlayQuizWithCategory(
                categoryId = comparisonQuizCategories.random(random).id
            )

            val comparisonPlayWithMode = ComparisonQuiz.PlayWithComparisonMode(
                mode = ComparisonMode.values().random(random)
            )

            val comparisonPlayAndGetScore = ComparisonQuiz.PlayAndGetScore(
                score = random.nextInt(5, 20)
            )

            val allTypes = eventsWithoutArgs() + listOf(
                multiChoicePlayQuizWithCategory,
                wordleWithType,
                comparisonPlayWithCategory,
                comparisonPlayWithMode,
                comparisonPlayAndGetScore
            )

            return allTypes.shuffled(random).take(count)
        }
    }

    sealed class MultiChoice(
        key: String,
        taskValueRange: UIntProgression
    ) : GameEvent(key, taskValueRange) {
        object PlayRandomQuiz : MultiChoice(
            key = "multi_choice_play_random_quiz",
            taskValueRange = 1u..5u
        )

        object EndQuiz : MultiChoice(
            key = "multi_choice_end_quiz",
            taskValueRange = 1u..5u
        )

        @Keep
        data class PlayQuizWithCategory(
            val categoryId: String
        ) : MultiChoice(
            key = "$KEY_PREFIX$categoryId",
            taskValueRange = 1u..5u
        ) {
            companion object {
                internal const val KEY_PREFIX = "multi_choice_play_quiz_category:"
            }
        }

        object PlayQuestions : MultiChoice(
            key = "multi_choice_play_questions",
            taskValueRange = 10u..50u step 10
        )

        object GetAnswersCorrect : MultiChoice(
            key = "multi_choice_get_questions_correct",
            taskValueRange = 5u..20u step 5
        )
    }

    sealed class Wordle(
        key: String,
        taskValueRange: UIntProgression
    ) : GameEvent(key, taskValueRange) {
        object GetWordCorrect : Wordle(
            key = "wordle_get_word_correct",
            taskValueRange = 1u..5u
        )

        @Keep
        data class PlayWordWithCategory(
            val wordleCategory: WordleQuizType
        ) : Wordle(
            key = "$KEY_PREFIX${wordleCategory.name}",
            taskValueRange = 1u..5u
        ) {
            companion object {
                internal const val KEY_PREFIX = "wordle_word_play_with_category:"
            }
        }
    }

    sealed class ComparisonQuiz(
        key: String,
        taskValueRange: UIntProgression
    ) : GameEvent(key, taskValueRange) {
        @Keep
        data class PlayQuizWithCategory(
            val categoryId: String
        ) : ComparisonQuiz(
            key = "$KEY_PREFIX$categoryId",
            taskValueRange = 1u..5u
        ) {
            companion object {
                internal const val KEY_PREFIX = "comparison_play_with_category:"
            }
        }

        @Keep
        data class PlayAndGetScore(
            val score: Int
        ) : ComparisonQuiz(
            key = "$KEY_PREFIX$score",
            taskValueRange = 1u..5u
        ) {
            companion object {
                internal const val KEY_PREFIX = "comparison_play_and_get_score:"
            }
        }

        @Keep
        data class PlayWithComparisonMode(
            val mode: ComparisonMode
        ) : ComparisonQuiz(
            key = "$KEY_PREFIX${mode.name}",
            taskValueRange = 1u..5u
        ) {
            companion object {
                internal const val KEY_PREFIX = "comparison_play_with_comparison_mode:"
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameEvent) return false

        return key == other.key
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}
