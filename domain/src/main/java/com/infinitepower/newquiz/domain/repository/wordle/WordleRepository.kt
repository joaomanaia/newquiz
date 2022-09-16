package com.infinitepower.newquiz.domain.repository.wordle

import com.infinitepower.newquiz.core.common.FlowResource

interface WordleRepository {
    suspend fun getAllWords(): Set<String>

    fun generateRandomWord(): FlowResource<String>

    fun isColorBlindEnabled(): FlowResource<Boolean>

    fun isLetterHintEnabled(): FlowResource<Boolean>

    fun isHardModeEnabled(): FlowResource<Boolean>

    suspend fun getWordleMaxRows(
        defaultMaxRow: Int? = null
    ): Int

    fun getAdRewardRowsToAdd(): Int
}