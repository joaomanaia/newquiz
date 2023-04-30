package com.infinitepower.newquiz.model.global_event

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.multi_choice_quiz.MultiChoiceCategory
import kotlin.random.Random

sealed class GameEvent(
    val key: String,
    val valueRange: UIntProgression
) {
    override fun toString(): String = key

    companion object {
        private fun eventsWithoutArgs(): List<GameEvent> = listOf(
            MultiChoice.PlayRandomQuiz,
            MultiChoice.EndQuiz,
            MultiChoice.PlayQuestions,
            MultiChoice.GetAnswersCorrect
        )

        fun fromKey(key: String): GameEvent {
            val eventsWithoutArgs = eventsWithoutArgs()

            return when {
                key.startsWith(MultiChoice.PlayQuizWithCategory.KEY_PREFIX) -> {
                    val categoryKey = key.substringAfter(MultiChoice.PlayQuizWithCategory.KEY_PREFIX)

                    MultiChoice.PlayQuizWithCategory(categoryKey)
                }
                eventsWithoutArgs.any { it.key == key } -> eventsWithoutArgs.first { it.key == key }
                else -> throw IllegalArgumentException("Unknown key: $key")
            }
        }

        fun getRandomEvents(
            count: Int,
            multiChoiceCategories: List<MultiChoiceCategory>,
            random: Random = Random
        ): List<GameEvent> {
            val multiChoicePlayQuizWithCategory = MultiChoice.PlayQuizWithCategory(
                categoryKey = multiChoiceCategories.random(random).key
            )

            val allTypes = eventsWithoutArgs() + multiChoicePlayQuizWithCategory

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
            val categoryKey: String
        ) : MultiChoice(
            key = "$KEY_PREFIX$categoryKey",
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameEvent) return false

        return key == other.key
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}
