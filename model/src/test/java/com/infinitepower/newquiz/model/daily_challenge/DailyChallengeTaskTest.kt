package com.infinitepower.newquiz.model.daily_challenge

import com.google.common.truth.Truth.assertThat
import com.infinitepower.newquiz.model.global_event.GameEvent
import com.infinitepower.newquiz.model.toUiText
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.time.Duration.Companion.days

/**
 * Tests for [DailyChallengeTask].
 */
internal class DailyChallengeTaskTest {
    @Test
    fun `test daily challenge task isDaily, when date range is one day long, returns true`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = false,
            dateRange = now.rangeTo(now + 1.days), // Not expired, one day long
            currentValue = 0u, // Not completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isDaily()).isTrue()
    }

    @Test
    fun `test daily challenge task isDaily, when date range is not one day long, returns false`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = false,
            dateRange = now.rangeTo(now + 5.days), // Not expired, one day long
            currentValue = 0u, // Not completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isDaily()).isFalse()
    }

    @Test
    fun `test when the current time is not in the task's date range`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = false,
            dateRange = (now - 2.days).rangeTo(now - 1.days), // Expired
            currentValue = 0u, // Not completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isExpired()).isTrue()
        assertThat(task.isCompleted()).isFalse()

        // The task is expired and not completed, so it is not active.
        assertThat(task.isActive()).isFalse()

        // The task is not completed, so it is not claimable.
        assertThat(task.isClaimable()).isFalse()
    }

    @Test
    fun `test when the current time is in the task's date range`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = false,
            dateRange = (now - 1.days).rangeTo(now + 1.days), // Not expired
            currentValue = 0u, // Not completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isExpired()).isFalse()
        assertThat(task.isCompleted()).isFalse()

        // The task is not expired and not completed, so it is active.
        assertThat(task.isActive()).isTrue()

        // The task is not completed, so it is not claimable.
        assertThat(task.isClaimable()).isFalse()
    }

    @Test
    fun `test when the current value is equal to the maximum value`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = false,
            dateRange = (now - 1.days).rangeTo(now + 1.days), // Not expired
            currentValue = 1u, // Completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isExpired()).isFalse()
        assertThat(task.isCompleted()).isTrue()

        // The task is not expired and not claimed, so it is active.
        assertThat(task.isActive()).isTrue()

        // The task is completed, so it is claimable.
        assertThat(task.isClaimable()).isTrue()
    }

    @Test
    fun `test when the task is already claimed`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = true,
            dateRange = (now - 1.days).rangeTo(now + 1.days), // Not expired
            currentValue = 1u, // Completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isExpired()).isFalse()
        assertThat(task.isCompleted()).isTrue()
        assertThat(task.isActive()).isFalse()
        assertThat(task.isClaimable()).isFalse()
    }

    @Test
    fun `test when the task is expired`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = false,
            dateRange = (now - 2.days).rangeTo(now - 1.days), // Expired
            currentValue = 1u, // Completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isExpired()).isTrue()
        assertThat(task.isCompleted()).isTrue()

        // The task is expired and completed, so it is not active.
        assertThat(task.isActive()).isFalse()

        // The task is completed, so it is claimable.
        assertThat(task.isClaimable()).isFalse()
    }

    @Test
    fun `test when the task is expired and already claimed`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = true,
            dateRange = (now - 2.days).rangeTo(now - 1.days), // Expired
            currentValue = 1u, // Completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isExpired()).isTrue()
        assertThat(task.isCompleted()).isTrue()

        // The task is expired and completed, so it is not active.
        assertThat(task.isActive()).isFalse()

        // The task is completed, so it is claimable.
        assertThat(task.isClaimable()).isFalse()
    }

    @Test
    fun `test when the task is expired and not completed and already claimed`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = true,
            dateRange = (now - 2.days).rangeTo(now - 1.days), // Expired
            currentValue = 0u, // Not completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isExpired()).isTrue()
        assertThat(task.isCompleted()).isFalse()

        // The task is expired and not completed, so it is not active.
        assertThat(task.isActive()).isFalse()

        // The task is not completed, so it is not claimable.
        assertThat(task.isClaimable()).isFalse()
    }

    // Test value out of range and date range out of range
    @Test
    fun `when current value is greater than the maximum value, should be completed`() {
        val now = Clock.System.now()

        val task = DailyChallengeTask(
            id = 1,
            diamondsReward = 1u,
            experienceReward = 1u,
            isClaimed = false,
            dateRange = (now - 1.days).rangeTo(now + 1.days), // Not expired
            currentValue = 2u, // Completed
            maxValue = 1u,
            event = GameEvent.MultiChoice.PlayQuestions,
            title = "Test".toUiText()
        )

        assertThat(task.isExpired()).isFalse()
        assertThat(task.isCompleted()).isTrue()
    }

    @Test
    fun `when the date range is invalid, should throw IllegalArgumentException`() {
        val now = Clock.System.now()

        val dateRange = (now + 1.days).rangeTo(now - 1.days)

        val tz = TimeZone.currentSystemDefault()
        val start = dateRange.start.toLocalDateTime(tz).date
        val end = dateRange.endInclusive.toLocalDateTime(tz).date

        val e = assertThrows<IllegalArgumentException> {
            DailyChallengeTask(
                id = 1,
                diamondsReward = 1u,
                experienceReward = 1u,
                isClaimed = false,
                dateRange = dateRange, // Not expired
                currentValue = 1u, // Completed
                maxValue = 1u,
                event = GameEvent.MultiChoice.PlayQuestions,
                title = "Test".toUiText()
            )
        }

        assertThat(e)
            .hasMessageThat()
            .isEqualTo("The start date ($start) must be less than or equal to the end date ($end).")
    }
}