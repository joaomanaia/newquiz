package com.infinitepower.newquiz.core.user_services.model.mappers

import com.infinitepower.newquiz.core.database.model.user.MultiChoiceGameResultEntity
import com.infinitepower.newquiz.model.game_result.MultiChoiceGameResult
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

internal fun MultiChoiceGameResult.toEntity(tz: TimeZone): MultiChoiceGameResultEntity {
    val playedAt = playedAt.toInstant(tz).toEpochMilliseconds()

    return MultiChoiceGameResultEntity(
        gameId = gameId,
        correctAnswers = correctAnswers.toInt(),
        questionCount = questionCount.toInt(),
        averageAnswerTime = averageAnswerTime,
        earnedXp = earnedXp.toInt(),
        playedAt = playedAt
    )
}

internal fun MultiChoiceGameResultEntity.toModel(tz: TimeZone): MultiChoiceGameResult {
    val playedAt = Instant.fromEpochMilliseconds(playedAt).toLocalDateTime(tz)

    return MultiChoiceGameResult(
        gameId = gameId,
        correctAnswers = correctAnswers.toUInt(),
        questionCount = questionCount.toUInt(),
        averageAnswerTime = averageAnswerTime,
        earnedXp = earnedXp.toUInt(),
        playedAt = playedAt
    )
}
