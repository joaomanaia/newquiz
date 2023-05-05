package com.infinitepower.newquiz.model.daily_challenge

import androidx.annotation.Keep
import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.global_event.GameEvent
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

/**
 * A challenge task.
 *
 * @property id The task's id.
 * @property diamondsReward The number of diamonds rewarded for completing the task.
 * @property experienceReward The number of experience points rewarded for completing the task.
 * @property isClaimed Whether the task has been claimed.
 * @property dateRange The date range in which the task is valid.
 * @property currentValue The current value of the task.
 * @property maxValue The maximum value of the task.
 *
 */
@Keep
data class DailyChallengeTask(
    val id: Int,
    val title: UiText,
    val diamondsReward: UInt,
    val experienceReward: UInt,
    val isClaimed: Boolean,
    val dateRange: ClosedRange<Instant>,
    val currentValue: UInt,
    val maxValue: UInt,
    val event: GameEvent
) {
    init {
        require(dateRange.start <= dateRange.endInclusive) {
            val tz = TimeZone.currentSystemDefault()
            val start = dateRange.start.toLocalDateTime(tz).date
            val end = dateRange.endInclusive.toLocalDateTime(tz).date

            "The start date ($start) must be less than or equal to the end date ($end)."
        }
    }

    /**
     * A task is daily if the [dateRange] is one day long.
     */
    fun isDaily(): Boolean {
        val start = dateRange.start
        val end = dateRange.endInclusive

        return start == end - 1.days
    }

    /**
     * A task is weekly if the [dateRange] is seven days long.
     */
    fun isWeekly(): Boolean {
        val start = dateRange.start
        val end = dateRange.endInclusive

        return start == end - 7.days
    }

    /**
     * A task is expired if the current time is not in the task's date range.
     * The function compares the current time to the task's date range.
     *
     * @return true if the task is expired, false otherwise.
     */
    fun isExpired(): Boolean {
        val now = Clock.System.now()

        return now !in dateRange
    }

    /**
     * A task is completed if the current [currentValue] is equal to the [maxValue].
     *
     * @return true if the task is completed, false otherwise.
     */
    fun isCompleted(): Boolean = currentValue >= maxValue

    /**
     * A task is claimable if it is completed and not claimed.
     *
     * @return true if the task is claimable, false otherwise.
     */
    fun isClaimable(): Boolean = !isExpired() && isCompleted() && !isClaimed

    /**
     * A task is active if it is not expired and not claimed.
     *
     * @return true if the task is active, false otherwise.
     */
    fun isActive(): Boolean = !isExpired() && !isClaimed

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DailyChallengeTask) return false

        val dateRangeEquals = dateRange.start.toEpochMilliseconds() == other.dateRange.start.toEpochMilliseconds()
                && dateRange.endInclusive.toEpochMilliseconds() == other.dateRange.endInclusive.toEpochMilliseconds()

        return id == other.id
                && diamondsReward == other.diamondsReward
                && experienceReward == other.experienceReward
                && isClaimed == other.isClaimed
                && dateRangeEquals
                && currentValue == other.currentValue
                && maxValue == other.maxValue
                && event == other.event
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + diamondsReward.hashCode()
        result = 31 * result + experienceReward.hashCode()
        result = 31 * result + isClaimed.hashCode()
        result = 31 * result + dateRange.hashCode()
        result = 31 * result + currentValue.hashCode()
        result = 31 * result + maxValue.hashCode()
        result = 31 * result + event.hashCode()
        return result
    }
}
