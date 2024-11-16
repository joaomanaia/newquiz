package com.infinitepower.newquiz.data.util.mappers.daily_challenge

import com.infinitepower.newquiz.core.database.model.DailyChallengeTaskEntity
import com.infinitepower.newquiz.data.repository.daily_challenge.util.getTitle
import com.infinitepower.newquiz.model.comparison_quiz.ComparisonQuizCategory
import com.infinitepower.newquiz.model.daily_challenge.DailyChallengeTask
import com.infinitepower.newquiz.model.global_event.GameEvent
import kotlinx.datetime.Instant

fun DailyChallengeTask.toEntity(): DailyChallengeTaskEntity {
    return DailyChallengeTaskEntity(
        id = id,
        diamondsReward = diamondsReward.toInt(),
        experienceReward = experienceReward.toInt(),
        isClaimed = isClaimed,
        currentValue = currentValue.toInt(),
        maxValue = maxValue.toInt(),
        type = event.key,
        startDate = dateRange.start.toEpochMilliseconds(),
        endDate = dateRange.endInclusive.toEpochMilliseconds()
    )
}

fun DailyChallengeTaskEntity.toModel(
    comparisonQuizCategories: List<ComparisonQuizCategory>
): DailyChallengeTask {
    val startInstant = Instant.fromEpochMilliseconds(startDate)
    val endInstant = Instant.fromEpochMilliseconds(endDate)

    val dateRange = startInstant..endInstant

    val type = GameEvent.fromKey(type)

    return DailyChallengeTask(
        id = id,
        diamondsReward = diamondsReward.toUInt(),
        experienceReward = experienceReward.toUInt(),
        isClaimed = isClaimed,
        dateRange = dateRange,
        currentValue = currentValue.toUInt(),
        maxValue = maxValue.toUInt(),
        event = type,
        title = type.getTitle(maxValue, comparisonQuizCategories)
    )
}
