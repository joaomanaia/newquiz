package com.infinitepower.newquiz.core.game

interface SkipGame {
    val skipCost: UInt

    suspend fun getUserDiamonds(): UInt

    suspend fun skip()
}
