package com.infinitepower.newquiz.model.daily_challenge

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.global_event.GameEvent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * Tests for [GameEvent].
 */
internal class DailyChallengeTaskTypeTest {
    @Test
    fun `test fromKey, when key is not valid, throws IllegalArgumentException`() {
        assertThrows<IllegalArgumentException> {
            GameEvent.fromKey("invalid")
        }
    }

    @Test
    fun `test fromKey, when key is valid, returns the correct type`() {
        val type = GameEvent.fromKey("multi_choice_play_questions")

        assertThat(type).isEqualTo(GameEvent.MultiChoice.PlayQuestions)
    }

    @Test
    fun `test fromKey, when key is PlayQuizWithCategory, returns the correct type`() {
        val keyPrefix = GameEvent.MultiChoice.PlayQuizWithCategory.KEY_PREFIX
        val categoryKey = "category_key"

        val type = GameEvent.fromKey("$keyPrefix$categoryKey")
        val expectedType = GameEvent.MultiChoice.PlayQuizWithCategory(categoryKey)

        assertThat(type).isEqualTo(expectedType)
    }
}
