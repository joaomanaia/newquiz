package com.infinitepower.newquiz.core.user_services

import com.infinitepower.newquiz.model.TimestampWithXP
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface XpManager {
    suspend fun getXpEarnedBy(start: Instant, end: Instant): List<TimestampWithXP>

    suspend fun getXpEarnedBy(timeRange: TimeRange): List<TimestampWithXP>

    fun getXpEarnedByFlow(timeRange: TimeRange): Flow<List<TimestampWithXP>>
}
