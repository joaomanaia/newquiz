package com.infinitepower.newquiz.core.user_services

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

interface XpManager {
    suspend fun getXpEarnedByRange(
        start: Instant,
        end: Instant
    ): XpEarnedByDays

    /**
     * Get the user's total XP for the current week
     */
    suspend fun getXpEarnedInLastDuration(
        duration: Duration = 7.days
    ): XpEarnedByDays {
        val now = Clock.System.now()
        val startDate = now - duration

        return getXpEarnedByRange(startDate, now)
    }

    fun getXpEarnedByRangeFlow(
        start: Instant,
        end: Instant
    ): Flow<XpEarnedByDays>

    /**
     * Get the user's total XP for the current week
     */
    fun getXpEarnedInLastDurationFlow(
        duration: Duration = 7.days
    ): Flow<XpEarnedByDays> {
        val now = Clock.System.now()
        val startDate = now - duration

        return getXpEarnedByRangeFlow(startDate, now)
    }
}