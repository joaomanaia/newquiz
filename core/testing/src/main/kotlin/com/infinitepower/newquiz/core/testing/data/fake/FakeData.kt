package com.infinitepower.newquiz.core.testing.data.fake

import com.infinitepower.newquiz.model.UiText
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import com.infinitepower.newquiz.model.global_event.GameEvent
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

object FakeData {
    fun generateTasks(
        count: Int = 10,
        dayDuration: ClosedRange<Duration> = 0.days..1.days
    ): List<DailyChallengeTask> {
        val now = Clock.System.now()
        val dateRange = (now + dayDuration.start)..(now + dayDuration.endInclusive)

        return List(count) { id -> generateTask(id + 1, dateRange) }
    }

    fun generateTask(id: Int, dateRange: ClosedRange<Instant>): DailyChallengeTask {
        return DailyChallengeTask(
            id = id,
            title = UiText.DynamicString("Task $id"),
            diamondsReward = 10u,
            experienceReward = 10u,
            isClaimed = false,
            dateRange = dateRange,
            currentValue = 0u,
            maxValue = 10u,
            event = GameEvent.MultiChoice.PlayRandomQuiz
        )
    }

    fun generateTasksWithOffset(
        size: Int = 10,
        instant: Instant = Clock.System.now(),
        offset: Duration = DEFAULT_TASKS_OFFSET.days // Offset to ensure tasks are in the 4 days in the past
    ): List<DailyChallengeTask> {
        return List(size) { day ->
            val startDate = instant + day.days + offset
            val endDate = instant + (day + 1).days + offset
            val dateRange = startDate..endDate

            generateTask(id = day + 1, dateRange)
        }
    }
}

private const val DEFAULT_TASKS_OFFSET = -4
