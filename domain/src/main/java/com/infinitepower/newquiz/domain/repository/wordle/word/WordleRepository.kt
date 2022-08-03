package com.infinitepower.newquiz.domain.repository.wordle.word

import com.infinitepower.newquiz.core.common.FlowResource

interface WordleRepository {
    suspend fun getAllWords(): Set<String>

    fun generateRandomWord(): FlowResource<String>
}