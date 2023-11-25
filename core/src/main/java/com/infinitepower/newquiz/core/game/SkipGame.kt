package com.infinitepower.newquiz.core.game

interface SkipGame {
    val skipCost: UInt

    suspend fun getUserDiamonds(): UInt

    suspend fun canSkip(): Boolean {
        val userDiamonds = getUserDiamonds()

        return userDiamonds >= skipCost
    }

    suspend fun skip()
}
