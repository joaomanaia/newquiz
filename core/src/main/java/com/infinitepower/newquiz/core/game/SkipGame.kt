package com.infinitepower.newquiz.core.game

interface SkipGame {
    val skipCost: UInt

    suspend fun getUserSkips(): UInt

    suspend fun canSkip(): Boolean {
        val skipCount = getUserSkips()

        return skipCount >= skipCost
    }

    suspend fun skip()
}
