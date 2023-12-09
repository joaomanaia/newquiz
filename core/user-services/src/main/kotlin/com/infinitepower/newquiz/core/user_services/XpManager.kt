package com.infinitepower.newquiz.core.user_services

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

typealias XpEarnedByDateTime = Map<Int, Int>

interface XpManager {
    suspend fun getXpEarnedBy(
        start: Instant,
        end: Instant
    ): XpEarnedByDateTime

    suspend fun getXpEarnedBy(
        timeRange: TimeRange
    ): XpEarnedByDateTime

    fun getXpEarnedByFlow(
        timeRange: TimeRange
    ): Flow<XpEarnedByDateTime>
}